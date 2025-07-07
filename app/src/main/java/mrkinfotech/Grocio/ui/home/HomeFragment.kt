package mrkinfotech.Grocio.ui.home

import ImageSlideAdapter
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import mrkinfotech.Grocio.R
import mrkinfotech.Grocio.databinding.FragmentFirstBinding
import mrkinfotech.Grocio.ui.Adapter.ItemAdapter


import mrkinfotech.Grocio.utils.MasterDataUtils

import kotlin.String


class HomeFragment : Fragment(){

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var imageSliderAdapter: ImageSlideAdapter
    private lateinit var itemList: ArrayList<ImageSlideAdapter>
    private lateinit var viewPager: ViewPager
    private var currentPage = 0
    private val handler = Handler(Looper.getMainLooper())
    private val crollDelays : Long = 3000 // 3 seconds
    private var runnable : Runnable? = null



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

        itemAdapter =
            ItemAdapter(
                requireContext(),
                MasterDataUtils.productitem(requireContext()),
                ItemAdapter.OnClickListener { itemData, clickType ->}
            )

        imageSliderAdapter = ImageSlideAdapter(requireContext(), MasterDataUtils.getviewPagerImage(requireContext()))

        binding.recyclerView.adapter = itemAdapter
        binding.viewpage.adapter = imageSliderAdapter

    }
    private fun startAutoScroll() {
        runnable = object : Runnable {
        override fun run() {
            if (MasterDataUtils.getviewPagerImage(requireContext()).isNotEmpty()) {
                currentPage =
                    (currentPage + 1) % MasterDataUtils.getviewPagerImage(requireContext()).size
                binding.viewpage.setCurrentItem(currentPage, true)
                handler.postDelayed(this, crollDelays)
            }
        }
    }
    handler.postDelayed(runnable!!, crollDelays)
}



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
