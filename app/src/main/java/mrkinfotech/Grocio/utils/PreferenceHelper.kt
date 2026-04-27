package mrkinfotech.Grocio.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import mrkinfotech.Grocio.ui.data.CommonDataClass
import mrkinfotech.Grocio.ui.data.LocationData
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
        private const val KEY_SAVED_ITEMS = "saved_items"
        private const val ITEM_SEPARATOR = "|~|"

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
            return getStoredStringSet(context, KEY_CART_ITEMS)
        }

        fun getSavedItems(context: Context): Set<String> {
            return getStoredStringSet(context, KEY_SAVED_ITEMS)
        }

        fun isItemInCart(context: Context, item: CommonDataClass): Boolean {
            return getCartItems(context).contains(buildItemKey(item.itemName))
        }

        fun isItemSaved(context: Context, item: CommonDataClass): Boolean {
            return getSavedItems(context).contains(buildItemKey(item.itemName))
        }

        fun addCartItem(context: Context, item: CommonDataClass): Boolean {
            return addItemMembership(context, KEY_CART_ITEMS, item.itemName)
        }

        fun removeCartItem(context: Context, item: CommonDataClass): Boolean {
            return removeItemMembership(context, KEY_CART_ITEMS, item.itemName)
        }

        fun toggleCartItem(context: Context, item: CommonDataClass): Boolean {
            return toggleItemMembership(context, KEY_CART_ITEMS, item.itemName)
        }

        fun addSavedItem(context: Context, item: CommonDataClass): Boolean {
            return addItemMembership(context, KEY_SAVED_ITEMS, item.itemName)
        }

        fun removeSavedItem(context: Context, item: CommonDataClass): Boolean {
            return removeItemMembership(context, KEY_SAVED_ITEMS, item.itemName)
        }

        fun toggleSavedItem(context: Context, item: CommonDataClass): Boolean {
            return toggleItemMembership(context, KEY_SAVED_ITEMS, item.itemName)
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
