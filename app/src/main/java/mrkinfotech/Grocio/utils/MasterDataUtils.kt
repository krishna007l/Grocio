package mrkinfotech.Grocio.utils

import android.content.Context
import mrkinfotech.Grocio.ui.data.itemDataclass

object MasterDataUtils {
    var itemson = ArrayList<itemDataclass>()
    fun contextlist(context: Context){


        itemson.add(
            itemDataclass(
                "apple",
                "$1.40",
                "https://en.m.wikipedia.org/wiki/File:Red_Apple.jpg" )
        )
        itemson.add(
            itemDataclass(
                "apple",
                "$0.50",
                "https://en.m.wikipedia.org/wiki/File:Red_Apple.jpg"
            )
        )
        itemson.add(
            itemDataclass(
                "mango",
                "$0.20",
                "https://www.shutterstock.com/image-photo/ripe-mango-green-leaf-isolated-on-2563853291"
            )
        )
        itemson.add(
            itemDataclass(
                "tomoto",
                "$0.10",
                "https://www.shutterstock.com/image-photo/ripe-mango-green-leaf-isolated-on-2563853291"
            )
        )
        itemson.add(
            itemDataclass(
                "orange",
                "$0.70",
                "https://www.shutterstock.com/image-photo/ripe-mango-green-leaf-isolated-on-2563853291"
            )
        )
        itemson.add(
            itemDataclass(
                "pinepal",
                "$0.10",
                "https://www.shutterstock.com/image-photo/ripe-mango-green-leaf-isolated-on-2563853291"
            )
        )

    }


}