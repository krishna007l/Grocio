package mrkinfotech.Grocio.utils

import android.content.Context
import mrkinfotech.Grocio.R
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

    fun viewPages(): ArrayList<String>{
        val page = ArrayList<Int>()
        page.add(R.drawable.apple)
        page.add(R.drawable.banana)
        page.add(R.drawable.mango)
        return TODO("Provide the return value")
    }

}