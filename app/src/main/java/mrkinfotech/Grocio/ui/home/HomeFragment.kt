package mrkinfotech.Grocio.ui.home

import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import android.os.Handler
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import mrkinfotech.Grocio.R
import mrkinfotech.Grocio.databinding.FragmentHomeBinding
import mrkinfotech.Grocio.ui.Adapter.ImageSlideAdapter
import mrkinfotech.Grocio.ui.Adapter.ItemAdapter
import mrkinfotech.Grocio.utils.MasterDataUtils

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var imageSlideAdapter: ImageSlideAdapter
    private var runnable: Runnable? = null
    private var currentPage = 0
    private val scrollDelay: Long = 3000
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        imageSlideAdapter =
            ImageSlideAdapter(requireContext(), MasterDataUtils.viewPages(requireContext()))
        binding.viewpage.adapter = imageSlideAdapter
        startAutoScroll()

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        itemAdapter = ItemAdapter(
            requireContext(), MasterDataUtils.productItem(requireContext()),
            ItemAdapter.OnClickListener { itemData, clickType -> })
        binding.recyclerView.adapter = itemAdapter

        binding.recyclerView.layoutAnimation = AnimationUtils.loadLayoutAnimation(requireContext(),R.anim.layout_animation_fall_down)
        binding.recyclerView.adapter = itemAdapter
        binding.recyclerView.scheduleLayoutAnimation()


        binding.recyclerView2.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        itemAdapter = ItemAdapter(
            requireContext(), MasterDataUtils.productItem(requireContext()),
            ItemAdapter.OnClickListener { itemData, clickType -> })
        binding.recyclerView2.adapter = itemAdapter

        binding.recyclerView2.layoutAnimation = AnimationUtils.loadLayoutAnimation(requireContext(),R.anim.layout_animation_fall_down)
        binding.recyclerView2.adapter = itemAdapter
        binding.recyclerView2.scheduleLayoutAnimation()


    }

    private fun startAutoScroll() {
        runnable = object : Runnable {
            override fun run() {
                if (MasterDataUtils.viewPages(requireContext()).isNotEmpty()) {
                    currentPage = (currentPage + 1) % MasterDataUtils.viewPages(requireContext()).size
                    binding.viewpage.setCurrentItem(currentPage, true)
                    handler.postDelayed(this, scrollDelay)
                }
            }
        }
        handler.postDelayed(runnable!!, scrollDelay)
    }


}