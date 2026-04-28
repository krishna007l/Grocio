package mrkinfotech.Grocio.utils

import android.content.Context
import mrkinfotech.Grocio.R
import mrkinfotech.Grocio.data.model.GroceryItem
import mrkinfotech.Grocio.ui.data.CommonDataClass
import mrkinfotech.Grocio.ui.data.ProductUiModel
import mrkinfotech.Grocio.ui.home.BannerCardUiModel
import mrkinfotech.Grocio.ui.home.HomeCategoryUiModel
import java.text.NumberFormat
import java.util.Locale

object MasterDataUtils {
    fun getCommonList(): ArrayList<CommonDataClass> {
        return ArrayList(getCatalog().filter { it.itemCategory == CATEGORY_FRUIT })
    }

    fun getPopularList(): ArrayList<CommonDataClass> {
        return ArrayList(getCatalog().filter { it.itemCategory == CATEGORY_VEGETABLE })
    }

    fun getAllProductsForHome(): ArrayList<CommonDataClass> {
        return ArrayList(getCatalog())
    }

    fun getProductList(): ArrayList<ProductUiModel> {
        return ArrayList(
            getAllItems().map {
                ProductUiModel(
                    imageUrl = it.imageUrl,
                    productName = it.itemName,
                    productPrice = it.itemPrice,
                    productCategory = it.itemCategory
                )
            }
        )
    }

    fun getProductsByCategory(categoryKey: String): ArrayList<ProductUiModel> {
        val normalizedCategory = normalizeCategoryKey(categoryKey)
        if (normalizedCategory.isBlank()) {
            return getProductList()
        }

        return ArrayList(
            getProductList().filter {
                normalizeCategoryKey(it.productCategory) == normalizedCategory
            }
        )
    }

    fun searchProducts(query: String): ArrayList<ProductUiModel> {
        val normalizedQuery = query.trim().lowercase(Locale.ROOT)
        if (normalizedQuery.isBlank()) {
            return getProductList()
        }

        return ArrayList(
            getProductList().filter {
                it.productName.lowercase(Locale.ROOT).contains(normalizedQuery) ||
                    it.productCategory.lowercase(Locale.ROOT).contains(normalizedQuery)
            }
        )
    }

    fun getViewPagerImage(): ArrayList<Int> {
        return arrayListOf(
            R.drawable.grapes_bunch,
            R.drawable.kiwi_fruit,
            R.drawable.pineapple_fruit,
            R.drawable.watermelon_fresh
        )
    }

    fun getHomeCategoryShortcuts(context: Context): ArrayList<HomeCategoryUiModel> {
        return arrayListOf(
            HomeCategoryUiModel(
                title = context.getString(R.string.str_home_category_vegetables),
                categoryKey = CATEGORY_VEGETABLE,
                badgeText = "V",
                subtitle = "Fresh picks",
                accentColorRes = R.color.color_green
            ),
            HomeCategoryUiModel(
                title = context.getString(R.string.str_home_category_fruits),
                categoryKey = CATEGORY_FRUIT,
                badgeText = "F",
                subtitle = "Seasonal",
                accentColorRes = R.color.color_yellow
            ),
            HomeCategoryUiModel(
                title = context.getString(R.string.str_home_category_dairy),
                categoryKey = CATEGORY_DAIRY,
                badgeText = "D",
                subtitle = "Breakfast",
                accentColorRes = R.color.color_surface_alt
            ),
            HomeCategoryUiModel(
                title = context.getString(R.string.str_home_category_snacks),
                categoryKey = CATEGORY_SNACKS,
                badgeText = "S",
                subtitle = "Munchies",
                accentColorRes = R.color.color_primary_dark
            )
        )
    }

    fun getBannerCards(): ArrayList<BannerCardUiModel> {
        return arrayListOf(
            BannerCardUiModel(
                imageUrl = toResourceImageUrl(R.drawable.grapes_bunch),
                badge = "Fresh",
                title = "Grapes",
                subtitle = "A fresh mixed-grape bunch from the new photo set.",
                accentColorRes = R.color.color_blinkit_yellow
            ),
            BannerCardUiModel(
                imageUrl = toResourceImageUrl(R.drawable.kiwi_fruit),
                badge = "Deal",
                title = "Kiwi Fruit",
                subtitle = "Bright, tangy kiwi for smoothies and healthy snacks.",
                accentColorRes = R.color.color_green
            ),
            BannerCardUiModel(
                imageUrl = toResourceImageUrl(R.drawable.pineapple_fruit),
                badge = "Tropical",
                title = "Pineapple",
                subtitle = "Fresh pineapple from the combined photo drops.",
                accentColorRes = R.color.color_surface_alt
            ),
            BannerCardUiModel(
                imageUrl = toResourceImageUrl(R.drawable.watermelon_fresh),
                badge = "Juicy",
                title = "Watermelon",
                subtitle = "Cool and refreshing summer fruit for quick restocks.",
                accentColorRes = R.color.color_primary_dark
            )
        )
    }

    fun getAllItems(): ArrayList<CommonDataClass> {
        return ArrayList(getCatalog().distinctBy { buildItemKey(it.itemName) })
    }

    fun getAllItems(context: Context): ArrayList<CommonDataClass> {
        return ArrayList(mergeCatalogWithStoredProducts(context).values)
    }

    fun getItemByKey(context: Context, itemName: String): CommonDataClass? {
        return mergeCatalogWithStoredProducts(context)[buildItemKey(itemName)]
    }

    fun getItemsByKeys(context: Context, itemKeys: Set<String>): ArrayList<CommonDataClass> {
        if (itemKeys.isEmpty()) return arrayListOf()
        val allItems = mergeCatalogWithStoredProducts(context)
        return ArrayList(itemKeys.mapNotNull { allItems[it] })
    }

    fun toCommonItem(item: GroceryItem): CommonDataClass {
        val normalizedPrice = item.price.trim()
        return CommonDataClass(
            image = 0,
            itemName = item.name.trim(),
            itemDescription = "Fresh grocery pick from today's live catalog, ready for quick delivery.",
            itemPrice = normalizedPrice.ifBlank { priceFormatter.format(0) },
            imageUrl = item.image.trim()
        )
    }

    fun parsePrice(itemPrice: String): Double {
        return itemPrice
            .replace(Regex("[^0-9.]"), "")
            .toDoubleOrNull()
            ?: 0.0
    }

    fun formatPrice(amount: Double): String {
        return priceFormatter.format(amount)
    }

    fun calculateCartTotal(items: Collection<CommonDataClass>): Double {
        return items.sumOf { item ->
            parsePrice(item.itemPrice) * item.quantity
        }
    }

    fun buildItemKey(itemName: String): String {
        return itemName.trim().lowercase(Locale.ROOT)
    }

    private fun mergeCatalogWithStoredProducts(context: Context): LinkedHashMap<String, CommonDataClass> {
        val mergedItems = linkedMapOf<String, CommonDataClass>()
        getAllItems().forEach { item -> 
            mergedItems[buildItemKey(item.itemName)] = item
        }
        PreferenceHelper.getProductSnapshots(context).values.forEach { item ->
            val key = buildItemKey(item.itemName)
            val catalogItem = mergedItems[key]
            mergedItems[key] = if (catalogItem == null) {
                item
            } else {
                item.copy(
                    image = if (item.image != 0) item.image else catalogItem.image,
                    itemDescription = item.itemDescription.ifBlank { catalogItem.itemDescription },
                    itemPrice = item.itemPrice.ifBlank { catalogItem.itemPrice },
                    itemCategory = item.itemCategory.ifBlank { catalogItem.itemCategory },
                    imageUrl = item.imageUrl.ifBlank { catalogItem.imageUrl }
                )
            }
        }
        return mergedItems
    }

    private const val CATEGORY_FRUIT = "fruit"
    private const val CATEGORY_VEGETABLE = "vegetable"
    private const val CATEGORY_DAIRY = "dairy"
    private const val CATEGORY_SNACKS = "snacks"
    private const val CATEGORY_PANTRY = CATEGORY_SNACKS

    private fun getCatalog(): List<CommonDataClass> {
        return listOf(
            catalogItem(R.drawable.hersheys_syrup, "Hershey's Syrup", "$4.50", CATEGORY_SNACKS),
            catalogItem(R.drawable.corn_on_cob, "Corn on Cob", "$1.90", CATEGORY_VEGETABLE),
            catalogItem(R.drawable.banana_bunch, "Banana Bunch", "$2.10", CATEGORY_FRUIT),
            catalogItem(R.drawable.lays_classic, "Lay's Classic", "$2.75", CATEGORY_SNACKS),
            catalogItem(R.drawable.tomato_ketchup, "Tomato Ketchup", "$3.05", CATEGORY_SNACKS),
            catalogItem(R.drawable.pineapple_fruit, "Pineapple", "$3.60", CATEGORY_FRUIT),
            catalogItem(R.drawable.cheetos_flamin_hot, "Cheetos Flamin' Hot", "$2.95", CATEGORY_SNACKS),
            catalogItem(R.drawable.chips_classic, "Chips Classic", "$2.40", CATEGORY_SNACKS),
            catalogItem(R.drawable.clover_chips, "Clover Chips", "$2.60", CATEGORY_SNACKS),
            catalogItem(R.drawable.cheese_cubes, "Cheese Cubes", "$4.20", CATEGORY_DAIRY),
            catalogItem(R.drawable.lays_hot_sweet_chilli, "Lay's Hot and Sweet Chilli", "$2.95", CATEGORY_SNACKS),
            catalogItem(R.drawable.cherries, "Cherries", "$5.40", CATEGORY_FRUIT),
            catalogItem(R.drawable.red_delicious_apple, "Red Delicious Apple", "$2.85", CATEGORY_FRUIT),
            catalogItem(R.drawable.premium_rice, "Premium Rice", "$8.20", CATEGORY_SNACKS),
            catalogItem(R.drawable.stik_o_wafer_sticks, "Stik-O Wafer Sticks", "$3.95", CATEGORY_SNACKS),
            catalogItem(R.drawable.alphonso_mango, "Alphonso Mango", "$4.80", CATEGORY_FRUIT),
            catalogItem(R.drawable.oreo_cookies, "Oreo Cookies", "$3.25", CATEGORY_SNACKS),
            catalogItem(R.drawable.grapes_bunch, "Grapes", "$3.35", CATEGORY_FRUIT),
            catalogItem(R.drawable.kiwi_fruit, "Kiwi Fruit", "$3.10", CATEGORY_FRUIT),
            catalogItem(R.drawable.strawberries_fresh, "Strawberries", "$3.80", CATEGORY_FRUIT),
            catalogItem(R.drawable.carrots_bunch, "Carrots", "$1.85", CATEGORY_VEGETABLE),
            catalogItem(R.drawable.potatoes_yellow, "Potatoes", "$1.70", CATEGORY_VEGETABLE),
            catalogItem(R.drawable.onions_bulb, "Onions", "$1.60", CATEGORY_VEGETABLE),
            catalogItem(R.drawable.eggplant_raw, "Eggplant", "$2.15", CATEGORY_VEGETABLE),
            catalogItem(R.drawable.spinach_bunch, "Spinach", "$1.75", CATEGORY_VEGETABLE),
            catalogItem(R.drawable.garlic_bulbs, "Garlic", "$1.45", CATEGORY_VEGETABLE),
            catalogItem(R.drawable.ginger_root, "Ginger", "$1.35", CATEGORY_VEGETABLE),
            catalogItem(R.drawable.peanuts_roasted, "Peanuts", "$2.05", CATEGORY_SNACKS),
            catalogItem(R.drawable.walnuts_halves, "Walnuts", "$6.40", CATEGORY_SNACKS),
            catalogItem(R.drawable.honey_jar, "Honey", "$5.25", CATEGORY_SNACKS),
            catalogItem(R.drawable.bread_loaf, "Bread", "$2.50", CATEGORY_SNACKS),
            catalogItem(R.drawable.dark_chocolate_bar, "Dark Chocolate", "$3.30", CATEGORY_SNACKS),
            catalogItem(R.drawable.almonds_raw, "Almonds", "$6.10", CATEGORY_SNACKS),
            catalogItem(R.drawable.radish_white, "Radish", "$1.55", CATEGORY_VEGETABLE),
            catalogItem(R.drawable.pineapple_slice, "Pineapple Slice", "$3.75", CATEGORY_FRUIT),
            catalogItem(R.drawable.guava_fruit, "Guava", "$3.10", CATEGORY_FRUIT),
            catalogItem(R.drawable.limes_green, "Limes", "$1.90", CATEGORY_FRUIT),
            catalogItem(R.drawable.green_peas, "Green Peas", "$2.30", CATEGORY_VEGETABLE),
            catalogItem(R.drawable.cauliflower_fresh, "Cauliflower", "$2.90", CATEGORY_VEGETABLE),
            catalogItem(R.drawable.papaya_fruit, "Papaya", "$3.85", CATEGORY_FRUIT),
            catalogItem(R.drawable.rambutan_fruit, "Rambutan", "$5.95", CATEGORY_FRUIT),
            catalogItem(R.drawable.tomato_fresh, "Tomato", "$2.20", CATEGORY_VEGETABLE),
            catalogItem(R.drawable.cashew_nuts, "Cashew Nuts", "$5.75", CATEGORY_SNACKS),
            catalogItem(R.drawable.dates_fruit, "Dates Fruit", "$4.90", CATEGORY_FRUIT),
            catalogItem(R.drawable.pistachios, "Pistachios", "$6.80", CATEGORY_SNACKS),
            catalogItem(R.drawable.beets, "Beets", "$2.45", CATEGORY_VEGETABLE),
            catalogItem(R.drawable.watermelon_fresh, "Watermelon", "$5.20", CATEGORY_FRUIT),
        )
    }

    private fun product(
        image: Int,
        itemName: String,
        itemDescription: String,
        itemPrice: String,
        itemCategory: String
    ): CommonDataClass {
        return CommonDataClass(
            image = image,
            itemName = itemName.trim(),
            itemDescription = itemDescription.trim(),
            itemPrice = itemPrice.trim(),
            itemCategory = itemCategory.trim(),
            imageUrl = toResourceImageUrl(image)
        )
    }

    private fun catalogItem(
        image: Int,
        itemName: String,
        itemPrice: String,
        itemCategory: String
    ): CommonDataClass {
        return product(
            image = image,
            itemName = itemName,
            itemDescription = "Fresh grocery pick from the photo set.",
            itemPrice = itemPrice,
            itemCategory = itemCategory
        )
    }

    private fun product(
        imageUrl: String,
        productName: String,
        productDescription: String,
        productPrice: String,
        productCategory: String
    ): CommonDataClass {
        return CommonDataClass(
            image = 0,
            itemName = productName.trim(),
            itemDescription = productDescription.trim(),
            itemPrice = productPrice.trim(),
            itemCategory = productCategory.trim(),
            imageUrl = imageUrl.trim()
        )
    }

    private val priceFormatter: NumberFormat =
        NumberFormat.getCurrencyInstance(Locale("en", "IN"))

    private fun toResourceImageUrl(drawableResId: Int): String {
        return "android.resource://mrkinfotech.Grocio/$drawableResId"
    }

    private fun normalizeCategoryKey(categoryKey: String): String {
        return when (categoryKey.trim().lowercase(Locale.ROOT)) {
            "", "all" -> ""
            "fruit", "fruits" -> CATEGORY_FRUIT
            "vegetable", "vegetables" -> CATEGORY_VEGETABLE
            "dairy" -> CATEGORY_DAIRY
            "snacks", "snack", "groceries", "grocery", "pantry" -> CATEGORY_SNACKS
            else -> categoryKey.trim().lowercase(Locale.ROOT)
        }
    }
}
