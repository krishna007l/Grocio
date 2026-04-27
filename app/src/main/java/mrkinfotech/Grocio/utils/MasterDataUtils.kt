package mrkinfotech.Grocio.utils

import mrkinfotech.Grocio.R
import mrkinfotech.Grocio.ui.data.CommonDataClass
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

    fun getViewPagerImage(): ArrayList<Int> {
        return arrayListOf(
            R.drawable.catalog_pineapple,
            R.drawable.catalog_strawberries,
            R.drawable.catalog_watermelon,
            R.drawable.catalog_papaya
        )
    }

    fun getAllItems(): ArrayList<CommonDataClass> {
        return ArrayList(getCatalog().distinctBy { buildItemKey(it.itemName) })
    }

    fun getItemsByKeys(itemKeys: Set<String>): ArrayList<CommonDataClass> {
        if (itemKeys.isEmpty()) return arrayListOf()
        return ArrayList(getAllItems().filter { buildItemKey(it.itemName) in itemKeys })
    }

    private fun buildItemKey(itemName: String): String {
        return itemName.trim().lowercase(Locale.ROOT)
    }

    private const val CATEGORY_FRUIT = "fruit"
    private const val CATEGORY_VEGETABLE = "vegetable"
    private const val CATEGORY_PANTRY = "pantry"

    private fun getCatalog(): List<CommonDataClass> {
        return listOf(
            product(
                R.drawable.apple,
                "Fresh Apple",
                "1 kg pack of crisp red apples, naturally sweet and perfect for snacks or salads.",
                "$3.99",
                CATEGORY_FRUIT
            ),
            product(
                R.drawable.banana,
                "Banana",
                "6 fresh bananas with a soft texture and clean sweetness for breakfast or shakes.",
                "$1.49",
                CATEGORY_FRUIT
            ),
            product(
                R.drawable.mango,
                "Mango",
                "Farm-fresh mangoes with juicy pulp and rich flavor, ideal for dessert or smoothies.",
                "$4.50",
                CATEGORY_FRUIT
            ),
            product(
                R.drawable.watermalen,
                "Watermelon",
                "1 whole watermelon with a juicy bite and refreshing taste for summer servings.",
                "$5.20",
                CATEGORY_FRUIT
            ),
            product(
                R.drawable.oreng,
                "Orange",
                "1 kg of bright oranges packed with citrus flavor and daily vitamin C support.",
                "$2.80",
                CATEGORY_FRUIT
            ),
            product(
                R.drawable.apple,
                "Green Apple",
                "500 g of tart green apples with a crunchy bite, great for fruit bowls and juices.",
                "$2.60",
                CATEGORY_FRUIT
            ),
            product(
                R.drawable.catalog_strawberries,
                "Strawberries",
                "200 g punnet of bright, juicy strawberries for desserts, smoothies, and breakfast bowls.",
                "$3.40",
                CATEGORY_FRUIT
            ),
            product(
                R.drawable.catalog_kiwi,
                "Kiwi",
                "4 ripe kiwis with vibrant green flesh and a sweet-tangy bite.",
                "$2.95",
                CATEGORY_FRUIT
            ),
            product(
                R.drawable.catalog_pineapple,
                "Pineapple",
                "1 sweet pineapple with fragrant flesh, ideal for fresh juice and fruit platters.",
                "$4.20",
                CATEGORY_FRUIT
            ),
            product(
                R.drawable.catalog_papaya,
                "Papaya",
                "1 ripe papaya with soft orange flesh, perfect for a light breakfast or smoothie.",
                "$3.85",
                CATEGORY_FRUIT
            ),
            product(
                R.drawable.catalog_grapes,
                "Mixed Grapes",
                "500 g of fresh mixed grapes with a juicy texture and naturally sweet taste.",
                "$4.10",
                CATEGORY_FRUIT
            ),
            product(
                R.drawable.catalog_limes,
                "Green Limes",
                "250 g of juicy green limes to brighten salads, drinks, and everyday cooking.",
                "$1.90",
                CATEGORY_FRUIT
            ),
            product(
                R.drawable.catalog_guava,
                "Guava",
                "500 g of tender guavas with pink flesh and a fragrant tropical flavor.",
                "$3.10",
                CATEGORY_FRUIT
            ),
            product(
                R.drawable.catalog_rambutan,
                "Rambutan",
                "500 g of fresh rambutans with sweet, floral fruit inside each shell.",
                "$5.95",
                CATEGORY_FRUIT
            ),
            product(
                R.drawable.catalog_tomato,
                "Tomato",
                "1 kg of ripe tomatoes for daily curries, salads, sandwiches, and sauces.",
                "$2.20",
                CATEGORY_VEGETABLE
            ),
            product(
                R.drawable.catalog_carrots,
                "Carrots",
                "1 kg of crunchy carrots rich in color and perfect for soups, salads, and snacks.",
                "$1.85",
                CATEGORY_VEGETABLE
            ),
            product(
                R.drawable.catalog_potatoes,
                "Potatoes",
                "1 kg of smooth all-purpose potatoes for fries, curries, roasting, or mashing.",
                "$1.70",
                CATEGORY_VEGETABLE
            ),
            product(
                R.drawable.catalog_onions,
                "Onions",
                "1 kg of kitchen onions with a balanced flavor for everyday meals.",
                "$1.60",
                CATEGORY_VEGETABLE
            ),
            product(
                R.drawable.catalog_spinach,
                "Spinach",
                "1 fresh bunch of leafy spinach for curries, soups, wraps, and healthy sides.",
                "$1.75",
                CATEGORY_VEGETABLE
            ),
            product(
                R.drawable.catalog_cauliflower,
                "Cauliflower",
                "1 medium cauliflower with tightly packed florets, fresh for stir-fry or roast.",
                "$2.90",
                CATEGORY_VEGETABLE
            ),
            product(
                R.drawable.catalog_radish,
                "White Radish",
                "1 crisp white radish with a clean bite for salads, pickles, and sabzi.",
                "$1.55",
                CATEGORY_VEGETABLE
            ),
            product(
                R.drawable.catalog_green_peas,
                "Green Peas",
                "500 g of tender green peas, sweet and fresh for pulao, curry, and pasta.",
                "$2.30",
                CATEGORY_VEGETABLE
            ),
            product(
                R.drawable.catalog_eggplant,
                "Eggplant",
                "500 g of glossy eggplants with a soft texture, great for bharta and curries.",
                "$2.15",
                CATEGORY_VEGETABLE
            ),
            product(
                R.drawable.catalog_garlic,
                "Garlic",
                "250 g of aromatic garlic bulbs to build bold flavor in every meal.",
                "$1.45",
                CATEGORY_VEGETABLE
            ),
            product(
                R.drawable.catalog_ginger,
                "Ginger",
                "200 g of fresh ginger root for tea, stir-fry, marinades, and home remedies.",
                "$1.35",
                CATEGORY_VEGETABLE
            ),
            product(
                R.drawable.catalog_beets,
                "Beets",
                "500 g of earthy red beets for juices, salads, roasting, and wholesome meals.",
                "$2.65",
                CATEGORY_VEGETABLE
            ),
            product(
                R.drawable.catalog_cashew_nuts,
                "Cashew Nuts",
                "250 g of whole cashews, creamy and crunchy for snacking or festive dishes.",
                "$5.75",
                CATEGORY_PANTRY
            ),
            product(
                R.drawable.catalog_almonds,
                "Almonds",
                "250 g of premium almonds for healthy snacking, milk, and breakfast toppings.",
                "$6.10",
                CATEGORY_PANTRY
            ),
            product(
                R.drawable.catalog_pistachios,
                "Pistachios",
                "200 g of roasted pistachios with a rich flavor and satisfying crunch.",
                "$6.80",
                CATEGORY_PANTRY
            ),
            product(
                R.drawable.catalog_walnuts,
                "Walnuts",
                "250 g of fresh walnut kernels, ideal for cereals, baking, and smart snacking.",
                "$6.40",
                CATEGORY_PANTRY
            ),
            product(
                R.drawable.catalog_peanuts,
                "Peanuts",
                "300 g of roasted peanuts for quick bites, chutneys, and homemade mixes.",
                "$2.05",
                CATEGORY_PANTRY
            ),
            product(
                R.drawable.catalog_dates,
                "Dates",
                "400 g of soft dates with natural sweetness, perfect for fasting and desserts.",
                "$4.45",
                CATEGORY_PANTRY
            ),
            product(
                R.drawable.catalog_honey,
                "Raw Honey",
                "500 g jar of golden honey with a smooth pour and naturally rich sweetness.",
                "$5.25",
                CATEGORY_PANTRY
            ),
            product(
                R.drawable.catalog_bread,
                "Whole Wheat Bread",
                "1 soft loaf of whole wheat bread for breakfast toast, sandwiches, and snacks.",
                "$2.50",
                CATEGORY_PANTRY
            ),
            product(
                R.drawable.catalog_dark_chocolate,
                "Dark Chocolate",
                "100 g bar of rich dark chocolate for gifting, desserts, or evening cravings.",
                "$3.30",
                CATEGORY_PANTRY
            )
        )
    }

    private fun product(
        image: Int,
        itemName: String,
        itemDescription: String,
        itemPrice: String,
        itemCategory: String
    ): CommonDataClass {
        return CommonDataClass(image, itemName, itemDescription, itemPrice, itemCategory)
    }
}
