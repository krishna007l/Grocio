package mrkinfotech.Grocio.utils

import android.content.Context
import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import mrkinfotech.Grocio.R
import mrkinfotech.Grocio.ui.Datamodel.CategaryItem
import mrkinfotech.Grocio.ui.Datamodel.ImageData
import mrkinfotech.Grocio.ui.Datamodel.itemDataclass

object MasterDataUtils {
    fun productItem(context: Context): ArrayList<itemDataclass>{
        val item = ArrayList<itemDataclass>()
        item.add(itemDataclass(
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
    fun Categaryitem(context: Context): ArrayList<CategaryItem>{
        val view = ArrayList<CategaryItem>()
        view.add(CategaryItem(R.drawable.apple))
        view.add(CategaryItem(R.drawable.banana))
        view.add(CategaryItem(R.drawable.google_icon))
        view.add(CategaryItem(R.drawable.eye_24))
        return view
    }

    fun viewPages(context: Context): ArrayList<ImageData>{
        val page = ArrayList<ImageData>()
        page.add(ImageData("https://www.collinsdictionary.com/images/full/apple_158989157_1000.jpg?version=6.0.84"))
        return page
    }
    fun countItem(context: Context, item: ArrayList<String>): Int {
        var  Citem = item.count()
        return Citem
    }



}