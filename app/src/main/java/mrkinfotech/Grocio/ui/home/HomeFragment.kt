package mrkinfotech.Grocio.ui.home

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import mrkinfotech.Grocio.R
import mrkinfotech.Grocio.databinding.FragmentFirstBinding
import mrkinfotech.Grocio.ui.Adapter.ItemAdapter
import mrkinfotech.Grocio.ui.data.itemDataclass


class HomeFragment : Fragment(){

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    lateinit var ItemAdapter: ItemAdapter
    var itemson = arrayListOf<itemDataclass>()
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
                val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        itemson = ArrayList()
        itemson.add(itemDataclass("hiten","500", R.drawable.login_image.toString()))
        itemson.add(itemDataclass("hiten","500", R.drawable.login_image.toString()))
        itemson.add(itemDataclass("hiten","500", R.drawable.login_image.toString()))

        ItemAdapter= ItemAdapter(requireContext(),itemson, onClickListener = {})
        recyclerView.adapter=ItemAdapter

    }
        override fun onDestroyView() {
            super.onDestroyView()
        _binding = null
    }

}