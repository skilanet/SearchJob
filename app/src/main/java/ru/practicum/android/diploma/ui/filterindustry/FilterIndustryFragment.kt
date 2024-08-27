package ru.practicum.android.diploma.ui.filterindustry

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.addCallback
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterIndustryBinding
import ru.practicum.android.diploma.domain.filterindustry.entity.FilterIndustryListState
import ru.practicum.android.diploma.domain.filterindustry.entity.FilterIndustryState
import ru.practicum.android.diploma.domain.referenceinfo.entity.Industry
import ru.practicum.android.diploma.util.BindingFragment

class FilterIndustryFragment : BindingFragment<FragmentFilterIndustryBinding>() {
    private val viewModel: FilterIndustryViewModel by viewModel()
    private val adapter by lazy {
        FilterIndustryAdapter { viewModel.onChecked(it) }
    }
    private var currentState: FilterIndustryListState? = null

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentFilterIndustryBinding {
        return FragmentFilterIndustryBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(
            view,
            savedInstanceState
        )
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel
        binding.recyclerIndustries.adapter = adapter
        binding.btnBack.setOnClickListener {
            viewModel.invalidateFilterChanges()
        }
        binding.buttonSave.setOnClickListener {
            findNavController().navigateUp()
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.invalidateFilterChanges()
        }
        viewModel.observeItems().observe(viewLifecycleOwner) {
            render(it)
            currentState = it
        }
        viewModel.observeChangesInvalidatedEvent().observe(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        viewModel.observeIndustryState().observe(viewLifecycleOwner) {
            showIndustryState(it)
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
            viewModel.onClearText()
        }
    }

    private fun setSearchIcon() {
        binding.textInputLayout.endIconDrawable = AppCompatResources.getDrawable(
            requireActivity(),
            R.drawable.search_ic
        )
        binding.textInputLayout.setEndIconOnClickListener {}
    }

    private fun showIndustryState(state: FilterIndustryState) {
        binding.buttonSave.isVisible = state.isSaveEnable
        adapter.applyFilter(state.filterText)
        if (adapter.itemCount == 0){
            binding.groupError.isVisible = true
            binding.imageError.setImageResource(R.drawable.empty_results_cat)
            binding.textErrorMessage.text = getString(R.string.failed_to_find_industry)
        } else {
            binding.groupError.isVisible = false
        }
        updateTextInputLayoutIcon(state.filterText)
    }

    private fun render(state: FilterIndustryListState) {
        when (state) {
            is FilterIndustryListState.Content -> showContent(state.industries, state.current)
            is FilterIndustryListState.Error -> showError()
        }
    }

    private fun showContent(industries: List<Industry>, current: Industry?) {
        binding.groupError.isVisible = false
        binding.recyclerIndustries.isVisible = true
        binding.buttonSave.isVisible = current != null
        adapter.setList(industries, current)
    }

    private fun showError() {
        binding.recyclerIndustries.isVisible = false
        binding.groupError.isVisible = true
        binding.buttonSave.isVisible = false
        binding.imageError.setImageResource(R.drawable.region_screen_placeholder_carpet)
        binding.textErrorMessage.text = getString(R.string.empty_list_error)
    }
}
