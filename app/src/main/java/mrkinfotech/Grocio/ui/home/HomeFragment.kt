package mrkinfotech.Grocio.ui.home

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import mrkinfotech.Grocio.R
import mrkinfotech.Grocio.databinding.FragmentFirstBinding
import mrkinfotech.Grocio.ui.Adapter.ItemAdapter
import mrkinfotech.Grocio.ui.Adapter.itemDataclass


class HomeFragment : Fragment(){

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    var itemson = arrayListOf<itemDataclass>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext()
            , LinearLayoutManager.VERTICAL, false)

        binding.recyclerView.adapter = ItemAdapter(requireContext()
            , itemson, ItemAdapter.OnClickListener { itemData, clickType ->

            })



    }
        override fun onDestroyView() {
            super.onDestroyView()
        _binding = null
    }
}