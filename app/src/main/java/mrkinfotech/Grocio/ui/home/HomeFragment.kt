package mrkinfotech.Grocio.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import mrkinfotech.Grocio.R
import mrkinfotech.Grocio.databinding.FragmentFirstBinding
import mrkinfotech.Grocio.ui.adapter.CommonItemAdapter
import mrkinfotech.Grocio.ui.adapter.ImageSliderAdapter
import mrkinfotech.Grocio.ui.data.CommonDataClass
import mrkinfotech.Grocio.ui.product.ProductDetailFragment
import mrkinfotech.Grocio.utils.CustomDialog
import mrkinfotech.Grocio.utils.MasterDataUtils
import mrkinfotech.Grocio.utils.MasterDataUtils.getViewPagerImage
import mrkinfotech.Grocio.utils.PreferenceHelper


class HomeFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private val handler = Handler(Looper.getMainLooper())
    private var autoScrollRunnable: Runnable? = null
    private var exclusiveAdapter: CommonItemAdapter? = null
    private var popularAdapter: CommonItemAdapter? = null
    private var allProductsAdapter: CommonItemAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = ImageSliderAdapter(requireContext(), getViewPagerImage())
        binding.viewPager.offscreenPageLimit = getViewPagerImage().size
        startAutoScroll()

        binding.recyclerViewExclusiveOffer.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL, false
        )
        exclusiveAdapter = CommonItemAdapter(
            requireContext(),
            MasterDataUtils.getCommonList(),
            CommonItemAdapter.ACTION_MODE_ADD_TO_CART,
            CommonItemAdapter.LAYOUT_MODE_CAROUSEL,
            CommonItemAdapter.OnClickListener { item, clickType ->
                handleItemInteraction(item, clickType)
            }
        )
        binding.recyclerViewExclusiveOffer.adapter = exclusiveAdapter

        binding.recyclerViewBestSelling.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL, false
        )
        popularAdapter = CommonItemAdapter(
            requireContext(),
            MasterDataUtils.getPopularList(),
            CommonItemAdapter.ACTION_MODE_ADD_TO_CART,
            CommonItemAdapter.LAYOUT_MODE_CAROUSEL,
            CommonItemAdapter.OnClickListener { item, clickType ->
                handleItemInteraction(item, clickType)
            }
        )
        binding.recyclerViewBestSelling.adapter = popularAdapter

        binding.recyclerViewAllProducts.layoutManager = GridLayoutManager(requireContext(), 2)
        allProductsAdapter = CommonItemAdapter(
            requireContext(),
            MasterDataUtils.getAllProductsForHome(),
            CommonItemAdapter.ACTION_MODE_ADD_TO_CART,
            CommonItemAdapter.LAYOUT_MODE_GRID,
            CommonItemAdapter.OnClickListener { item, clickType ->
                handleItemInteraction(item, clickType)
            }
        )
        binding.recyclerViewAllProducts.adapter = allProductsAdapter

        refreshProductInteractionState()
    }

    override fun onResume() {
        super.onResume()
        refreshProductInteractionState()
    }

    private fun handleItemInteraction(item: CommonDataClass, clickType: Int) {
        if (clickType == CommonItemAdapter.CLICK_TYPE_CARD) {
            val navController = findNavController()
            if (navController.currentDestination?.id == R.id.HomeFragment) {
                navController.navigate(
                    R.id.ProductDetailFragment,
                    ProductDetailFragment.createArgs(item)
                )
            }
            return
        }

        val isInCart = PreferenceHelper.isItemInCart(requireContext(), item)
        val wasAdded = if (isInCart) {
            false
        } else {
            PreferenceHelper.addCartItem(requireContext(), item)
        }
        refreshProductInteractionState()
        CustomDialog.showToast(
            requireContext(),
            if (wasAdded) getString(R.string.str_item_added_to_cart)
            else getString(R.string.str_opening_cart)
        )
        (activity as? HomeMainActivity)?.openCartScreen()
    }

    private fun refreshProductInteractionState() {
        val fragmentContext = context ?: return
        val cartItems = PreferenceHelper.getCartItems(fragmentContext)
        val savedItems = PreferenceHelper.getSavedItems(fragmentContext)
        exclusiveAdapter?.updateInteractionState(cartItems, savedItems)
        popularAdapter?.updateInteractionState(cartItems, savedItems)
        allProductsAdapter?.updateInteractionState(cartItems, savedItems)
    }

    private fun startAutoScroll() {
        val images = getViewPagerImage()
        autoScrollRunnable?.let(handler::removeCallbacks)
        autoScrollRunnable = object : Runnable {
            override fun run() {
                val localBinding = _binding ?: return
                if (images.isNotEmpty()) {
                    val nextPage = (localBinding.viewPager.currentItem + 1) % images.size
                    localBinding.viewPager.setCurrentItem(nextPage, true)
                    handler.postDelayed(this, 4000)
                }
            }
        }
        if (images.size > 1) {
            handler.postDelayed(autoScrollRunnable!!, 4000)
        }
    }

    override fun onDestroyView() {
        autoScrollRunnable?.let(handler::removeCallbacks)
        autoScrollRunnable = null
        _binding = null
        super.onDestroyView()
    }
}
