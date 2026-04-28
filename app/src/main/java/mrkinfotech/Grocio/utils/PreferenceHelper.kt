package mrkinfotech.Grocio.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import mrkinfotech.Grocio.ui.data.CheckoutDetails
import mrkinfotech.Grocio.ui.data.CommonDataClass
import mrkinfotech.Grocio.ui.data.LocationData
import mrkinfotech.Grocio.ui.data.OrderData
import mrkinfotech.Grocio.ui.data.ProfileData
import java.util.Locale

class PreferenceHelper {
    companion object {
        private const val PREFS_NAME = "Grocio"
        private const val USER_EMAIL = "UserEmail"
        private const val USER_NAME = "UserName"
        private const val KEY_USER_AGE = "userAge"
        private const val KEY_ONBOARDING_SHOW = "ONBOARDING_SHOW"
        private const val KEY_USER_GENDER = "userGender"
        private const val KEY_PROFILE_IMAGE = "profileImage"
        private const val USER_ID = "UserID"
        private const val KEY_LAST_LATITUDE = "last_latitude"
        private const val KEY_LAST_LONGITUDE = "last_longitude"
        private const val KEY_LAST_ADDRESS = "last_address"
        private const val KEY_CART_ITEMS = "cart_items"
        private const val KEY_CART_ITEM_QUANTITIES = "cart_item_quantities"
        private const val KEY_SAVED_ITEMS = "saved_items"
        private const val KEY_PRODUCT_SNAPSHOTS = "product_snapshots"
        private const val KEY_ORDER_HISTORY = "order_history"
        private const val KEY_LAST_CHECKOUT_DETAILS = "last_checkout_details"
        private const val ITEM_SEPARATOR = "|~|"
        private val gson: Gson by lazy { Gson() }

        fun getSharedPrefs(context: Context): SharedPreferences {
            return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        }

        fun getUserEmail(context: Context): String? {
            return readSanitizedString(getSharedPrefs(context), USER_EMAIL).ifBlank { null }
        }

        fun setUserEmail(context: Context, email: String?) {
            getSharedPrefs(context).edit().putString(USER_EMAIL, email).commit()
        }

        fun isUserLoggedIn(context: Context): Boolean {
            return !getUserEmail(context).isNullOrBlank()
        }

        fun getOnBoardShow(context: Context): Boolean {
            return getSharedPrefs(context).getBoolean(KEY_ONBOARDING_SHOW, false)
        }

        fun setOnBoardShow(context: Context, onShow: Boolean) {
            getSharedPrefs(context).edit().putBoolean(KEY_ONBOARDING_SHOW, onShow).commit()
        }

        fun saveProfileData(context: Context, profile: ProfileData?) {
            getSharedPrefs(context).edit {
                putString(USER_EMAIL, sanitizeStoredValue(profile?.email))
                putString(USER_NAME, sanitizeStoredValue(profile?.name))
                putInt(KEY_USER_AGE, profile?.age ?: 0)
                putString(KEY_USER_GENDER, sanitizeStoredValue(profile?.gender))
                putString(KEY_PROFILE_IMAGE, sanitizeStoredValue(profile?.imageUri))
                putString(KEY_LAST_ADDRESS, sanitizeStoredValue(profile?.address))
            }
        }

        fun getProfileData(context: Context): ProfileData {
            val prefs = getSharedPrefs(context)
            return ProfileData(
                name = readSanitizedString(prefs, USER_NAME),
                email = readSanitizedString(prefs, USER_EMAIL),
                age = try {
                    prefs.getInt(KEY_USER_AGE, 0)
                } catch (e: ClassCastException) {
                    prefs.getString(KEY_USER_AGE, "0")?.toIntOrNull() ?: 0
                },
                gender = readSanitizedString(prefs, KEY_USER_GENDER),
                imageUri = readSanitizedString(prefs, KEY_PROFILE_IMAGE),
                address = readSanitizedString(prefs, KEY_LAST_ADDRESS)
            )
        }

        fun setLastLocation(context: Context, locationData: LocationData) {
            getSharedPrefs(context).edit {
                putFloat(KEY_LAST_LATITUDE, locationData.latitude.toFloat())
                putFloat(KEY_LAST_LONGITUDE, locationData.longitude.toFloat())
            }
        }

        fun getLastLocation(context: Context): LocationData {
            val prefs = getSharedPrefs(context)
            return LocationData(
                prefs.getFloat(KEY_LAST_LATITUDE, 0f).toDouble(),
                prefs.getFloat(KEY_LAST_LONGITUDE, 0f).toDouble(),
            )
        }

        fun getCartItems(context: Context): Set<String> {
            return getCartItemQuantities(context).keys
        }

        fun getCartItemQuantities(context: Context): Map<String, Int> {
            val prefs = getSharedPrefs(context)
            val rawValue = try {
                prefs.getString(KEY_CART_ITEM_QUANTITIES, null)
            } catch (e: ClassCastException) {
                null
            }

            val storedQuantities = if (!rawValue.isNullOrBlank()) {
                runCatching {
                    gson.fromJson<Map<String, Int>>(
                        rawValue,
                        object : TypeToken<Map<String, Int>>() {}.type
                    )
                }.getOrNull().orEmpty()
            } else {
                emptyMap()
            }

            if (storedQuantities.isNotEmpty()) {
                return storedQuantities
            }

            val legacyKeys = getStoredStringSet(context, KEY_CART_ITEMS)
            if (legacyKeys.isEmpty()) {
                return emptyMap()
            }

            val migratedQuantities = linkedMapOf<String, Int>().apply {
                legacyKeys.forEach { put(it, 1) }
            }
            persistCartItemQuantities(context, migratedQuantities)
            return migratedQuantities
        }

        fun getCartItemsWithQuantity(context: Context): List<CommonDataClass> {
            val quantities = getCartItemQuantities(context)
            if (quantities.isEmpty()) return emptyList()

            return quantities.mapNotNull { (itemKey, quantity) ->
                val item = getProductSnapshot(context, itemKey)
                item?.copy(quantity = quantity) ?: run {
                    CommonDataClass(
                        image = 0,
                        itemName = itemKey,
                        itemDescription = "",
                        itemPrice = "",
                        quantity = quantity
                    )
                }
            }
        }

        fun getSavedItems(context: Context): Set<String> {
            return getStoredStringSet(context, KEY_SAVED_ITEMS)
        }

        fun getProductSnapshots(context: Context): Map<String, CommonDataClass> {
            return readJsonValue(
                context = context,
                key = KEY_PRODUCT_SNAPSHOTS,
                typeToken = object : TypeToken<Map<String, CommonDataClass>>() {},
                defaultValue = emptyMap()
            )
        }

        fun getProductSnapshot(context: Context, itemName: String): CommonDataClass? {
            return getProductSnapshots(context)[buildItemKey(itemName)]
        }

        fun saveProductSnapshot(context: Context, item: CommonDataClass) {
            saveProductSnapshots(context, listOf(item))
        }

        fun saveProductSnapshots(context: Context, items: Collection<CommonDataClass>) {
            if (items.isEmpty()) return

            val storedItems = getProductSnapshots(context).toMutableMap()
            items.forEach { item ->
                val normalizedItem = normalizeStoredProduct(item)
                storedItems[buildItemKey(normalizedItem.itemName)] = normalizedItem
            }
            persistJsonValue(context, KEY_PRODUCT_SNAPSHOTS, storedItems)
        }

        fun getOrders(context: Context): List<OrderData> {
            return readJsonValue(
                context = context,
                key = KEY_ORDER_HISTORY,
                typeToken = object : TypeToken<List<OrderData>>() {},
                defaultValue = emptyList()
            )
        }

        fun addOrder(context: Context, order: OrderData) {
            val storedOrders = getOrders(context).toMutableList()
            storedOrders.add(0, order)
            persistJsonValue(context, KEY_ORDER_HISTORY, storedOrders)
        }

        fun getLastCheckoutDetails(context: Context): CheckoutDetails {
            return readJsonValue(
                context = context,
                key = KEY_LAST_CHECKOUT_DETAILS,
                typeToken = object : TypeToken<CheckoutDetails>() {},
                defaultValue = CheckoutDetails()
            )
        }

        fun saveLastCheckoutDetails(context: Context, checkoutDetails: CheckoutDetails) {
            persistJsonValue(context, KEY_LAST_CHECKOUT_DETAILS, checkoutDetails)
        }

        fun isItemInCart(context: Context, item: CommonDataClass): Boolean {
            return (getCartItemQuantities(context)[buildItemKey(item.itemName)] ?: 0) > 0
        }

        fun isItemInCart(context: Context, itemName: String): Boolean {
            return (getCartItemQuantities(context)[buildItemKey(itemName)] ?: 0) > 0
        }

        fun isItemSaved(context: Context, item: CommonDataClass): Boolean {
            return getSavedItems(context).contains(buildItemKey(item.itemName))
        }

        fun isItemSaved(context: Context, itemName: String): Boolean {
            return getSavedItems(context).contains(buildItemKey(itemName))
        }

        fun addCartItem(context: Context, item: CommonDataClass): Int {
            return addCartItem(context, item.itemName)
        }

        fun addCartItem(context: Context, itemName: String): Int {
            return addCartItem(context, itemName, 1)
        }

        fun addCartItem(context: Context, item: CommonDataClass, quantity: Int): Int {
            return addCartItem(context, item.itemName, quantity)
        }

        fun addCartItem(context: Context, itemName: String, quantity: Int): Int {
            val safeQuantity = quantity.coerceAtLeast(1)
            val quantities = getCartItemQuantities(context).toMutableMap()
            val normalizedItemName = buildItemKey(itemName)
            val newQuantity = (quantities[normalizedItemName] ?: 0) + safeQuantity
            quantities[normalizedItemName] = newQuantity
            persistCartItemQuantities(context, quantities)
            return newQuantity
        }

        fun removeCartItem(context: Context, item: CommonDataClass): Int {
            return removeCartItem(context, item.itemName)
        }

        fun removeCartItem(context: Context, itemName: String): Int {
            val quantities = getCartItemQuantities(context).toMutableMap()
            val normalizedItemName = buildItemKey(itemName)
            val currentQuantity = quantities[normalizedItemName] ?: 0
            if (currentQuantity <= 0) {
                return 0
            }

            val newQuantity = currentQuantity - 1
            if (newQuantity <= 0) {
                quantities.remove(normalizedItemName)
            } else {
                quantities[normalizedItemName] = newQuantity
            }
            persistCartItemQuantities(context, quantities)
            return newQuantity
        }

        fun removeCartItemCompletely(context: Context, item: CommonDataClass): Boolean {
            return removeCartItemCompletely(context, item.itemName)
        }

        fun removeCartItemCompletely(context: Context, itemName: String): Boolean {
            val quantities = getCartItemQuantities(context).toMutableMap()
            val normalizedItemName = buildItemKey(itemName)
            if (!quantities.containsKey(normalizedItemName)) {
                return false
            }

            quantities.remove(normalizedItemName)
            persistCartItemQuantities(context, quantities)
            return true
        }

        fun toggleCartItem(context: Context, item: CommonDataClass): Int {
            return toggleCartItem(context, item.itemName)
        }

        fun toggleCartItem(context: Context, itemName: String): Int {
            val itemKey = buildItemKey(itemName)
            val quantities = getCartItemQuantities(context).toMutableMap()
            val newQuantity = if (quantities.containsKey(itemKey)) {
                quantities.remove(itemKey)
                0
            } else {
                quantities[itemKey] = 1
                1
            }
            persistCartItemQuantities(context, quantities)
            return newQuantity
        }

        fun addSavedItem(context: Context, item: CommonDataClass): Boolean {
            return addItemMembership(context, KEY_SAVED_ITEMS, item.itemName)
        }

        fun addSavedItem(context: Context, itemName: String): Boolean {
            return addItemMembership(context, KEY_SAVED_ITEMS, itemName)
        }

        fun removeSavedItem(context: Context, item: CommonDataClass): Boolean {
            return removeItemMembership(context, KEY_SAVED_ITEMS, item.itemName)
        }

        fun removeSavedItem(context: Context, itemName: String): Boolean {
            return removeItemMembership(context, KEY_SAVED_ITEMS, itemName)
        }

        fun toggleSavedItem(context: Context, item: CommonDataClass): Boolean {
            return toggleItemMembership(context, KEY_SAVED_ITEMS, item.itemName)
        }

        fun toggleSavedItem(context: Context, itemName: String): Boolean {
            return toggleItemMembership(context, KEY_SAVED_ITEMS, itemName)
        }

        fun clear(context: Context) {
            getSharedPrefs(context).edit().clear().commit()
        }

        fun clearUserSession(context: Context) {
            val hasCompletedOnboarding = getOnBoardShow(context)
            getSharedPrefs(context).edit()
                .clear()
                .putBoolean(KEY_ONBOARDING_SHOW, hasCompletedOnboarding)
                .commit()
        }

        fun clearCartItems(context: Context) {
            persistCartItemQuantities(context, emptyMap())
        }

        private fun getStoredStringSet(context: Context, key: String): Set<String> {
            val prefs = getSharedPrefs(context)
            return try {
                prefs.getString(key, "")?.split(ITEM_SEPARATOR)
                    ?.map { it.trim() }
                    ?.filter { it.isNotBlank() }
                    ?.toSet()
                    ?: emptySet()
            } catch (e: ClassCastException) {
                val legacyItems = prefs.getStringSet(key, emptySet())?.toSet() ?: emptySet()
                persistStoredItems(context, key, legacyItems)
                legacyItems
            }
        }

        private fun toggleItemMembership(context: Context, key: String, itemName: String): Boolean {
            val storedItems = getStoredStringSet(context, key).toMutableSet()
            val normalizedItemName = buildItemKey(itemName)
            val wasAdded = if (storedItems.contains(normalizedItemName)) {
                storedItems.remove(normalizedItemName)
                false
            } else {
                storedItems.add(normalizedItemName)
                true
            }

            getSharedPrefs(context).edit {
                putString(key, storedItems.joinToString(ITEM_SEPARATOR))
            }
            return wasAdded
        }

        private fun addItemMembership(context: Context, key: String, itemName: String): Boolean {
            val storedItems = getStoredStringSet(context, key).toMutableSet()
            val normalizedItemName = buildItemKey(itemName)
            if (!storedItems.add(normalizedItemName)) {
                return false
            }
            persistStoredItems(context, key, storedItems)
            return true
        }

        private fun removeItemMembership(context: Context, key: String, itemName: String): Boolean {
            val storedItems = getStoredStringSet(context, key).toMutableSet()
            val normalizedItemName = buildItemKey(itemName)
            if (!storedItems.remove(normalizedItemName)) {
                return false
            }
            persistStoredItems(context, key, storedItems)
            return true
        }

        private fun persistStoredItems(context: Context, key: String, items: Set<String>) {
            getSharedPrefs(context).edit {
                putString(key, items.joinToString(ITEM_SEPARATOR))
            }
        }

        private fun persistCartItemQuantities(context: Context, items: Map<String, Int>) {
            getSharedPrefs(context).edit {
                putString(KEY_CART_ITEM_QUANTITIES, gson.toJson(items))
                putString(KEY_CART_ITEMS, items.keys.joinToString(ITEM_SEPARATOR))
            }
        }

        private fun <T> readJsonValue(
            context: Context,
            key: String,
            typeToken: TypeToken<T>,
            defaultValue: T
        ): T {
            val rawValue = try {
                getSharedPrefs(context).getString(key, null)
            } catch (e: ClassCastException) {
                null
            }

            if (rawValue.isNullOrBlank()) {
                return defaultValue
            }

            return runCatching {
                gson.fromJson<T>(rawValue, typeToken.type)
            }.getOrNull() ?: defaultValue
        }

        private fun persistJsonValue(context: Context, key: String, value: Any) {
            getSharedPrefs(context).edit {
                putString(key, gson.toJson(value))
            }
        }

        private fun normalizeStoredProduct(item: CommonDataClass): CommonDataClass {
            return item.copy(
                itemName = item.itemName.trim(),
                itemDescription = item.itemDescription.trim(),
                itemPrice = item.itemPrice.trim(),
                itemCategory = item.itemCategory.trim(),
                imageUrl = item.imageUrl.trim(),
                quantity = 0
            )
        }

        private fun sanitizeStoredValue(value: String?): String? {
            return value
                ?.trim()
                ?.takeUnless { it.isBlank() || it.equals("null", ignoreCase = true) }
        }

        private fun readSanitizedString(prefs: SharedPreferences, key: String): String {
            return try {
                sanitizeStoredValue(prefs.getString(key, null)).orEmpty()
            } catch (e: ClassCastException) {
                ""
            }
        }

        private fun buildItemKey(itemName: String): String {
            return itemName.trim().lowercase(Locale.ROOT)
        }
    }
}
