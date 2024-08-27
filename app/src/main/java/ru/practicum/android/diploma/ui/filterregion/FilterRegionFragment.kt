package ru.practicum.android.diploma.ui.filterregion

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterRegionBinding
import ru.practicum.android.diploma.domain.filter.entity.AreaEntity
import ru.practicum.android.diploma.presentation.filterregion.RegionFilterViewModel
import ru.practicum.android.diploma.presentation.filterregion.state.RegionFilterState
import ru.practicum.android.diploma.ui.filterregion.adapters.RegionListAdapter
import ru.practicum.android.diploma.util.BindingFragment

class FilterRegionFragment : BindingFragment<FragmentFilterRegionBinding>() {
    private val regionViewModel: RegionFilterViewModel by viewModel()
    private val adapter by lazy {
        RegionListAdapter { area ->
            addRegionToFilter(area)
        }
    }

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentFilterRegionBinding {
        return FragmentFilterRegionBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewmodel = regionViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.recyclerRegions.adapter = adapter

        setSearchIcon()

        binding.editTextSearchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                regionViewModel.onEditorActionDone()
            }
            false
        }

        regionViewModel.observeSearchTextLiveData().observe(viewLifecycleOwner) {
            updateTextInputLayoutIcon(it)
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        regionViewModel.getScreenStateLiveData().observe(viewLifecycleOwner) {
            renderState(it)
        }

        regionViewModel.observeAddRegionAddedEvent().observe(viewLifecycleOwner) {
            findNavController().navigateUp()
        }
    }

    private fun updateTextInputLayoutIcon(text: String) {
        if (text.isNotEmpty()) {
            setClearIcon()
        } else {
            setSearchIcon()
        }

    }

    private fun setClearIcon() {
        binding.textInputLayout.endIconDrawable = AppCompatResources.getDrawable(
            requireActivity(),
            R.drawable.close_ic
        )
        binding.textInputLayout.setEndIconOnClickListener {
            val inputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(
                Activity().currentFocus?.windowToken,
                0
            )
            regionViewModel.onClearText()
            adapter.restoreOriginal()
            resetScreenState()
            binding.recyclerRegions.isVisible = true
        }
    }

    private fun setSearchIcon() {
        binding.textInputLayout.endIconDrawable = AppCompatResources.getDrawable(
            requireActivity(),
            R.drawable.search_ic
        )
        binding.textInputLayout.setEndIconOnClickListener {}
    }

    private fun resetScreenState() {
        binding.recyclerRegions.isVisible = false
        binding.layoutError.isVisible = false
    }

    private fun renderError() {
        resetScreenState()
        binding.layoutError.isVisible = true
        binding.textError.text = getString(R.string.empty_list_error)
        binding.imageError.setImageResource(R.drawable.region_screen_placeholder_carpet)
    }

    private fun renderEmpty() {
        resetScreenState()
        binding.layoutError.isVisible = true
        binding.textError.text = getString(R.string.this_region_does_not_exist)
        binding.imageError.setImageResource(R.drawable.empty_results_cat)
    }

    private fun renderContent(regions: List<AreaEntity>) {
        resetScreenState()
        adapter.setItems(regions)
        binding.recyclerRegions.isVisible = true
    }

    private fun renderFilter(query: String) {
        resetScreenState()
        binding.recyclerRegions.isVisible = true
        adapter.filter(query)
        if (adapter.isEmpty()) {
            renderEmpty()
        }
    }

    private fun renderState(state: RegionFilterState) {
        when (state) {
            is RegionFilterState.Filter -> renderFilter(state.query)
            is RegionFilterState.Error -> renderError()
            is RegionFilterState.Content -> renderContent(state.regions)
        }
    }

    private fun addRegionToFilter(region: AreaEntity) {
        regionViewModel.addRegionToFilter(region)
    }
}
