package mrkinfotech.Grocio.ui.home


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import mrkinfotech.Grocio.R
import mrkinfotech.Grocio.databinding.ActivityMainBinding


class HomeMainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    var itemson = arrayListOf<itemDataclass>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        itemson = ArrayList<itemDataclass>()


        itemson.add(
            itemDataclass(
                "apple",
                "$0.50",
                "https://en.m.wikipedia.org/wiki/File:Red_Apple.jpg"            )
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

        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.recyclerView.adapter = ItemAdapter(this, itemson, ItemAdapter.OnClickListener {itemData,clickType ->

        })
    }
}