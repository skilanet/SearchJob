package ru.practicum.android.diploma.ui.filtersettings

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.addCallback
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterSettingsBinding
import ru.practicum.android.diploma.domain.filter.entity.Filter
import ru.practicum.android.diploma.presentation.filtersettings.FilterSettingsViewModel
import ru.practicum.android.diploma.util.BindingFragment

class FilterSettingsFragment : BindingFragment<FragmentFilterSettingsBinding>() {
    private val filterSettingsViewModel: FilterSettingsViewModel by viewModel()

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentFilterSettingsBinding {
        return FragmentFilterSettingsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        filterSettingsViewModel.observeScreenStateLiveData().observe(viewLifecycleOwner) { state ->
            renderFilterInfo(state.filterSettings)
        }

        filterSettingsViewModel.observeApplyButtonLiveData().observe(viewLifecycleOwner) {
            binding.btnApply.isEnabled = it
        }

        filterSettingsViewModel.observeFiltersAddedEvent().observe(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        filterSettingsViewModel.observeResetButtonLiveData().observe(viewLifecycleOwner) {
            binding.btnReset.isVisible = !it
        }

        filterSettingsViewModel.observeSalaryTextLiveData().observe(viewLifecycleOwner) {
            updateSalaryTextLayoutIcon(it)
        }

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = filterSettingsViewModel
        setUpListeners()
    }

    private fun setUpListeners() {
        binding.btnApply.setOnClickListener {
            filterSettingsViewModel.applyFilters(true)
        }
        binding.edittextVacancyRegion.setOnClickListener {
            findNavController().navigate(R.id.action_filterSettingsFragment_to_filterLocationFragment)
        }
        binding.edittextVacancyType.setOnClickListener {
            findNavController().navigate(R.id.action_filterSettingsFragment_to_filterIndustryFragment)
        }
        binding.checkedTextView.setOnClickListener {
            binding.checkedTextView.toggle()
            filterSettingsViewModel.addSalaryCheckFilter(binding.checkedTextView.isChecked)
        }
        binding.btnReset.setOnClickListener {
            filterSettingsViewModel.resetFilters()
        }
        binding.btnBack.setOnClickListener {
            filterSettingsViewModel.applyFilters(false)
        }
        binding.edittextSalary.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                filterSettingsViewModel.onActionDone()
            }
            false
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            filterSettingsViewModel.applyFilters(false)
        }
    }

    private fun renderFilterInfo(filter: Filter) {
        binding.edittextSalary.setText(filter.salary.toString())
        if (filter.area?.country?.name != null) {
            binding.edittextVacancyRegion.setText(filter.area!!.country?.name)
            if (filter.area?.region?.name != null) {
                binding.edittextVacancyRegion.setText(
                    getString(
                        R.string.country_and_region,
                        filter.area?.country?.name,
                        filter.area?.region?.name
                    )
                )
            }
            setClearIconRegion()
        } else {
            binding.edittextVacancyRegion.setText("")
        }
        if (filter.salary?.salary != null) {
            binding.edittextSalary.setText(filter.salary?.salary.toString())
            setSalaryClearIcon()
        } else {
            binding.edittextSalary.setText("")
        }
        if (filter.salary?.onlyWithSalary != null) {
            binding.checkedTextView.isChecked = filter.salary?.onlyWithSalary!!
        } else {
            binding.checkedTextView.isChecked = false
        }
        if (filter.industry?.name != null) {
            binding.edittextVacancyType.setText(filter.industry?.name)
            setClearIconIndustry()
        } else {
            binding.edittextVacancyType.setText("")
        }

    }

    private fun setForwardArrowRegion() {
        binding.textlayoutVacancyRegion.endIconDrawable = AppCompatResources.getDrawable(
            requireContext(),
            R.drawable.arrow_forward_ic
        )
        binding.textlayoutVacancyRegion.setEndIconOnClickListener {
            findNavController().navigate(R.id.action_filterSettingsFragment_to_filterLocationFragment)
        }
    }

    private fun updateSalaryTextLayoutIcon(text: String) {
        if (text.isNotEmpty()) {
            setSalaryClearIcon()
        } else {
            resetSalaryIcon()
        }
    }

    private fun setClearIconRegion() {
        binding.textlayoutVacancyRegion.endIconDrawable = AppCompatResources.getDrawable(
            requireContext(),
            R.drawable.close_ic
        )
        binding.textlayoutVacancyRegion.setEndIconOnClickListener {
            filterSettingsViewModel.clearRegion()
            setForwardArrowRegion()
        }
    }

    private fun setForwardArrowIndustry() {
        binding.textlayoutVacancyType.endIconDrawable = AppCompatResources.getDrawable(
            requireContext(),
            R.drawable.arrow_forward_ic
        )
        binding.textlayoutVacancyType.setEndIconOnClickListener {
            findNavController().navigate(R.id.action_filterSettingsFragment_to_filterIndustryFragment)
        }
    }

    private fun setClearIconIndustry() {
        binding.textlayoutVacancyType.endIconDrawable = AppCompatResources.getDrawable(
            requireContext(),
            R.drawable.close_ic
        )
        binding.textlayoutVacancyType.setEndIconOnClickListener {
            filterSettingsViewModel.clearIndustry()
            setForwardArrowIndustry()
        }
    }

    private fun setSalaryClearIcon() {
        binding.textlayoutSalary.endIconDrawable = AppCompatResources.getDrawable(
            requireContext(),
            R.drawable.close_ic
        )
        binding.textlayoutSalary.setEndIconOnClickListener {
            filterSettingsViewModel.clearSalary()
            val inputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(
                Activity().currentFocus?.windowToken,
                0
            )
        }
    }

    private fun resetSalaryIcon() {
        binding.textlayoutSalary.endIconDrawable = null
        binding.textlayoutSalary.setEndIconOnClickListener { Unit }
    }

    override fun onResume() {
        super.onResume()
        filterSettingsViewModel.updateFilterData()
    }

    companion object {
        const val APPLY_FILTERS_KEY = "APPLY_FILTERS_KEY"
    }
}
