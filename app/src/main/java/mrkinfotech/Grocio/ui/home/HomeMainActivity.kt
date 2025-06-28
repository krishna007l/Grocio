package mrkinfotech.Grocio.ui.home


import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import mrkinfotech.Grocio.R
import mrkinfotech.Grocio.databinding.ActivityMainBinding


class HomeMainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    var itemson =arrayListOf<itemdataclass>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        itemson = ArrayList<itemdataclass>()


        itemson.add(itemdataclass("apple","$0.50"))
        itemson.add(itemdataclass("mango","$0.20",))
        itemson.add(itemdataclass("tomoto","$0.10",))
        itemson.add(itemdataclass("orange","$0.70"))
        itemson.add(itemdataclass("pinepal","$0.10",))

        binding.recyclerView.layoutManager= LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

        binding.recyclerView.adapter= ProductAdapter(this, itemson)


    }
}