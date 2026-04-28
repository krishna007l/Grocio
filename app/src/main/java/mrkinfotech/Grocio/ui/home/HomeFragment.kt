package mrkinfotech.Grocio.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import mrkinfotech.Grocio.R
import mrkinfotech.Grocio.data.remote.RetrofitProvider
import mrkinfotech.Grocio.data.repository.GroceryRepository
import mrkinfotech.Grocio.databinding.FragmentFirstBinding
import mrkinfotech.Grocio.utils.CustomDialog
import mrkinfotech.Grocio.utils.MasterDataUtils
import mrkinfotech.Grocio.utils.PreferenceHelper

class HomeFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private val groceryAdapter by lazy {
        GroceryItemAdapter(
            onItemClicked = { item ->
                openItemSheet(item)
            },
            onAddToCartClicked = { item ->
                openItemSheet(item)
            },
            onLikeClicked = { item ->
                val isLiked = viewModel.toggleLike(requireContext(), item)
                CustomDialog.showToast(
                    requireContext(),
                    if (isLiked) getString(R.string.str_home_item_liked)
                    else getString(R.string.str_home_item_unliked)
                )
            }
        )
    }

    private val viewModel: HomeViewModel by lazy {
        val repository = GroceryRepository(RetrofitProvider.groceryApiService)
        ViewModelProvider(
            this,
            HomeViewModelFactory(repository)
        )[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        bindActions()
        observeState()
        viewModel.refreshLocalState(requireContext())
    }

    override fun onResume() {
        super.onResume()
        if (isAdded) {
            viewModel.refreshLocalState(requireContext())
        }
    }

    private fun setupRecyclerView() {
        val spanCount = when (resources.configuration.screenWidthDp) {
            in 600..839 -> 3
            in 840..Int.MAX_VALUE -> 4
            else -> 2
        }

        binding.recyclerViewGroceries.apply {
            layoutManager = GridLayoutManager(requireContext(), spanCount)
            adapter = groceryAdapter
            setHasFixedSize(true)
        }
    }

    private fun bindActions() {
        binding.buttonRetry.setOnClickListener {
            viewModel.loadGroceries()
        }
    }

    private fun observeState() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            render(state)
        }
    }

    private fun render(state: HomeUiState) {
        if (state.groceries.isNotEmpty()) {
            PreferenceHelper.saveProductSnapshots(
                requireContext(),
                state.groceries.map(MasterDataUtils::toCommonItem)
            )
        }

        groceryAdapter.submitList(state.groceries)
        groceryAdapter.updateInteractionState(
            state.cartItemQuantities.keys,
            PreferenceHelper.getSavedItems(requireContext())
        )

        val hasItems = state.groceries.isNotEmpty()
        val hasError = !state.errorMessage.isNullOrBlank()

        binding.layoutLoadingState.isVisible = state.isLoading
        binding.recyclerViewGroceries.isVisible = hasItems && !state.isLoading
        binding.layoutErrorState.isVisible = !state.isLoading && (hasError || !hasItems)
        binding.buttonRetry.isVisible = hasError

        binding.textViewStatus.text = when {
            state.isLoading -> getString(R.string.str_home_loading_items)
            hasItems -> getString(R.string.str_home_loaded_count, state.groceries.size)
            hasError -> getString(R.string.str_home_error_badge)
            else -> getString(R.string.str_home_empty_badge)
        }

        binding.textViewErrorTitle.text = if (hasError) {
            getString(R.string.str_home_error_title)
        } else {
            getString(R.string.str_home_empty_title)
        }
        binding.textViewErrorMessage.text = if (hasError) {
            state.errorMessage
        } else {
            getString(R.string.str_home_empty_message)
        }
    }

    private fun openItemSheet(item: mrkinfotech.Grocio.data.model.GroceryItem) {
        if (childFragmentManager.findFragmentByTag(GroceryItemBottomSheetFragment.TAG) != null) {
            return
        }
        GroceryItemBottomSheetFragment.show(this, item)
    }

    override fun onDestroyView() {
        binding.recyclerViewGroceries.adapter = null
        _binding = null
        super.onDestroyView()
    }
}
