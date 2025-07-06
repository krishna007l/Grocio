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
import mrkinfotech.Grocio.ui.Adapter.ImageSlideAdapter
import mrkinfotech.Grocio.ui.Adapter.ItemAdapter
import mrkinfotech.Grocio.ui.Adapter.itemDataclass
import mrkinfotech.Grocio.utils.MasterDataUtils
import kotlin.String


class HomeFragment : Fragment(){

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private lateinit var itemAdapter: ItemAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
            View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager= LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        itemAdapter = ItemAdapter(requireContext(),MasterDataUtils.productitem(requireContext()),
                ItemAdapter.OnClickListener { itemData, clickType -> })


        binding.recyclerView.adapter = itemAdapter
        binding.viewpage.adapter = ImageSlideAdapter(requireContext(), MasterDataUtils.viewpages())

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}