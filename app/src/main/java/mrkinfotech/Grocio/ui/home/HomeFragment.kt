package mrkinfotech.Grocio.ui.home

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import mrkinfotech.Grocio.R
import mrkinfotech.Grocio.databinding.FragmentFirstBinding
import mrkinfotech.Grocio.ui.data.ProductUiModel
import mrkinfotech.Grocio.ui.home.HomeCategoryUiModel
import mrkinfotech.Grocio.utils.CustomDialog
import mrkinfotech.Grocio.utils.MasterDataUtils
import mrkinfotech.Grocio.utils.PreferenceHelper

class HomeFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private val categoryAdapter by lazy {
        CategoryShortcutAdapter { category -> onCategorySelected(category) }
    }

    private val productAdapter by lazy {
        ProductListAdapter(
            onAddToCartClicked = { item -> addToCart(item) }
        )
    }

    private val bannerAdapter by lazy { BannerSliderAdapter() }

    private var selectedCategoryKey: String = CATEGORY_ALL
    private val cartStateListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key == CART_PREF_KEY || key == CART_LEGACY_PREF_KEY) {
            syncCartState()
        }
        if (
            key == PROFILE_NAME_KEY ||
            key == PROFILE_EMAIL_KEY ||
            key == PROFILE_ADDRESS_KEY ||
            key == PROFILE_IMAGE_KEY
        ) {
            syncLocation()
        }
    }

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

        setupLocationHeader()
        setupCategoryAndProductLists()
        setupBannerSlider()
        setupActions()
        renderProducts()
        registerCartListener()
    }

    override fun onResume() {
        super.onResume()
        syncCartState()
    }

    private fun setupCategoryAndProductLists() {
        binding.recyclerViewCategories.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryAdapter
            setHasFixedSize(true)
        }

        binding.recyclerViewGroceries.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = productAdapter
            setHasFixedSize(true)
        }

        categoryAdapter.submitList(MasterDataUtils.getHomeCategoryShortcuts(requireContext()))
    }

    private fun setupActions() {
        binding.cardHomeLocation.setOnClickListener {
            findNavController().navigate(R.id.ProfileFragment)
        }

        binding.cardSearchShortcut.setOnClickListener {
            findNavController().navigate(R.id.SearchFragment)
        }
    }

    private fun setupLocationHeader() {
        syncLocation()
    }

    private fun setupBannerSlider() {
        binding.viewPagerBannerSlider.apply {
            adapter = bannerAdapter
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3

            val transformer = CompositePageTransformer().apply {
                addTransformer(MarginPageTransformer(resources.getDimensionPixelSize(R.dimen.margin_12)))
                addTransformer { page, position ->
                    val absPosition = kotlin.math.abs(position)
                    val scale = 0.94f + (1f - absPosition).coerceAtLeast(0f) * 0.06f
                    page.scaleY = scale
                }
            }
            setPageTransformer(transformer)
        }
        bannerAdapter.submitList(MasterDataUtils.getBannerCards())
    }

    private fun renderProducts() {
        val products = filterProducts()
        categoryAdapter.updateSelection(selectedCategoryKey)
        productAdapter.submitList(products)
        syncCartState()
        syncLocation()
    }

    private fun filterProducts(): List<ProductUiModel> {
        return MasterDataUtils.getProductsByCategory(selectedCategoryKey)
    }

    private fun addToCart(item: ProductUiModel) {
        PreferenceHelper.addCartItem(requireContext(), item.productName)
        CustomDialog.showToast(requireContext(), getString(R.string.str_item_added_to_cart))
        syncCartState()
    }

    private fun onCategorySelected(category: HomeCategoryUiModel) {
        selectedCategoryKey = if (selectedCategoryKey == category.categoryKey) {
            CATEGORY_ALL
        } else {
            category.categoryKey
        }
        renderProducts()
    }

    private fun syncCartState() {
        if (!isAdded || _binding == null) return
        productAdapter.updateCartState(PreferenceHelper.getCartItems(requireContext()))
    }

    private fun syncLocation() {
        if (!isAdded || _binding == null) return
        val profile = PreferenceHelper.getProfileData(requireContext())
        binding.textHomeLocationValue.text = profile.address.ifBlank {
            getString(R.string.str_home_location_default)
        }
    }

    private fun registerCartListener() {
        PreferenceHelper.getSharedPrefs(requireContext())
            .registerOnSharedPreferenceChangeListener(cartStateListener)
    }

    override fun onDestroyView() {
        if (context != null) {
            PreferenceHelper.getSharedPrefs(requireContext())
                .unregisterOnSharedPreferenceChangeListener(cartStateListener)
        }
        binding.recyclerViewGroceries.adapter = null
        _binding = null
        super.onDestroyView()
    }

    companion object {
        private const val CATEGORY_ALL = "all"
        private const val CART_PREF_KEY = "cart_item_quantities"
        private const val CART_LEGACY_PREF_KEY = "cart_items"
        private const val PROFILE_NAME_KEY = "UserName"
        private const val PROFILE_EMAIL_KEY = "UserEmail"
        private const val PROFILE_ADDRESS_KEY = "last_address"
        private const val PROFILE_IMAGE_KEY = "profileImage"
    }
}
