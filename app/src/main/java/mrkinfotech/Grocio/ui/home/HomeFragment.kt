package mrkinfotech.Grocio.ui.home

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.os.Handler
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import mrkinfotech.Grocio.databinding.FragmentFirstBinding
import mrkinfotech.Grocio.ui.Adapter.ImageSlideAdapter
import mrkinfotech.Grocio.ui.Adapter.ItemAdapter
import mrkinfotech.Grocio.utils.MasterDataUtils

class HomeFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var imageSlideAdapter: ImageSlideAdapter
    private var runnable: Runnable? = null
    private var currentPage = 0
    private val scrollDelay: Long = 3000
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        itemAdapter = ItemAdapter(
            requireContext(), MasterDataUtils.productItem(requireContext()),
            ItemAdapter.OnClickListener { itemData, clickType -> })
        imageSlideAdapter = ImageSlideAdapter(requireContext(), MasterDataUtils.viewPages())

        binding.recyclerView.adapter = itemAdapter
        binding.viewpage.adapter = imageSlideAdapter
        startAutoScroll()
    }

    private fun startAutoScroll() {
        runnable = object : Runnable {
            override fun run() {
                if (MasterDataUtils.viewPages().isNotEmpty()) {
                    currentPage =
                        (currentPage + 1) % MasterDataUtils.viewPages().size
                    binding.viewpage.setCurrentItem(currentPage, true)
                    handler.postDelayed(this, scrollDelay)
                }
            }
        }
        handler.postDelayed(runnable!!, scrollDelay)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}