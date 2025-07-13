package mrkinfotech.Grocio.utils

import android.content.Context
import mrkinfotech.Grocio.R
import mrkinfotech.Grocio.ui.Datamodel.ItemViewData
import mrkinfotech.Grocio.ui.Datamodel.itemDataclass

object MasterDataUtils {
    fun productItem(context: Context): ArrayList<itemDataclass>{
        val item = ArrayList<itemDataclass>()
        item.add(
            itemDataclass(
                nameProduct = "apple" ,
                priceProduct = "$1.40" ,
                R.drawable.apple
            )
        )

        item.add(
            itemDataclass(
                nameProduct = "orenge" ,
                priceProduct = "$2.00" ,
                R.drawable.oreng
            )
        )
        item.add(
            itemDataclass(
                nameProduct = "banana" ,
                priceProduct = "$1.23" ,
                R.drawable.banana
            )
        )
        item.add(
            itemDataclass(
                nameProduct = "mango" ,
                priceProduct = "$1.12" ,
                R.drawable.mango
            )
        )
        item.add(
            itemDataclass(
                nameProduct = "watermalen" ,
                priceProduct = "$1.99" ,
                R.drawable.watermalen
            )
        )
        return item

    }

    fun viewPages(context: Context): ArrayList<ItemViewData>{
        val page = ArrayList<ItemViewData>()
        page.add(ItemViewData(R.drawable.apple))
        page.add(ItemViewData(R.drawable.banana))
        page.add(ItemViewData(R.drawable.mango))
        return page
    }

    fun countItem(context: Context, item: ArrayList<String>): Int {
        var  Citem = item.count()
        return Citem
    }
}


