package mrkinfotech.Grocio.utils

import android.content.Context
import android.os.Message
import android.view.View
import android.widget.Toast
import mrkinfotech.Grocio.R
import mrkinfotech.Grocio.ui.Adapter.itemDataclass

object MasterDataUtils {
    fun productitem(context: Context): ArrayList<itemDataclass>{
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
    fun itemData(){

    }



}