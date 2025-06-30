package mrkinfotech.Grocio.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import mrkinfotech.Grocio.R
import mrkinfotech.Grocio.databinding.ActivityMainBinding
import mrkinfotech.Grocio.ui.Adapter.ItemAdapter
import mrkinfotech.Grocio.ui.Adapter.itemDataclass


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
                "$1.40",
                "https://en.m.wikipedia.org/wiki/File:Red_Apple.jpg"            )
        )

        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.recyclerView.adapter =
            ItemAdapter(this, itemson, ItemAdapter.OnClickListener { itemData, clickType ->

            })
    }
}