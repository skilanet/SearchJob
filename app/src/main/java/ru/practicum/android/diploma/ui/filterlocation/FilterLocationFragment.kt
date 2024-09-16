package ru.practicum.android.diploma.ui.filterlocation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterLocationBinding
import ru.practicum.android.diploma.presentation.filterlocation.LocationFilterViewModel
import ru.practicum.android.diploma.presentation.filterlocation.state.LocationState
import ru.practicum.android.diploma.util.BindingFragment

class FilterLocationFragment : BindingFragment<FragmentFilterLocationBinding>() {
    private val locationFilterViewModel: LocationFilterViewModel by viewModel()

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentFilterLocationBinding {
        return FragmentFilterLocationBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationFilterViewModel.observeScreenStateLiveData().observe(viewLifecycleOwner) {
            renderState(it)
        }
        locationFilterViewModel.observeCachedRegionInvalidatedEvent().observe(viewLifecycleOwner) {
            findNavController().navigateUp()
        }
        binding.edittextVacancyCountry.setOnClickListener {
            findNavController().navigate(R.id.action_filterLocationFragment_to_filterCountryFragment)
        }
        binding.edittextVacancyRegion.setOnClickListener {
            findNavController().navigate(R.id.action_filterLocationFragment_to_filterRegionFragment)
        }
        binding.btnBack.setOnClickListener {
            locationFilterViewModel.invalidateChangedLocationFilter()
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            locationFilterViewModel.invalidateChangedLocationFilter()
        }
        binding.btnApply.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onResume() {
        super.onResume()
        locationFilterViewModel.getSavedLocationFilterSettings()
    }

    private fun renderState(state: LocationState) {
        val country = state.country
        val region = state.region
        binding.btnApply.isVisible = country != null || region != null
        binding.edittextVacancyCountry.setText(country?.name ?: "")
        if (country != null) {
            setXiconCountry()
        }
        binding.edittextVacancyRegion.setText(region?.name ?: "")
        if (region != null) {
            setXiconRegion()
        }
    }

    private fun setForwardArrowCountry() {
        binding.textlayoutVacancyCountry.endIconDrawable = AppCompatResources.getDrawable(
            requireContext(),
            R.drawable.arrow_forward_ic
        )
        binding.textlayoutVacancyCountry.setEndIconOnClickListener { }
    }

    private fun setXiconCountry() {
        binding.textlayoutVacancyCountry.endIconDrawable = AppCompatResources.getDrawable(
            requireContext(),
            R.drawable.close_ic
        )
        binding.textlayoutVacancyCountry.setEndIconOnClickListener {
            locationFilterViewModel.clearCountry()
            setForwardArrowCountry()
            setForwardArrowRegion()
        }
    }

    private fun setForwardArrowRegion() {
        binding.textlayoutVacancyRegion.endIconDrawable = AppCompatResources.getDrawable(
            requireContext(),
            R.drawable.arrow_forward_ic
        )
        binding.textlayoutVacancyRegion.setEndIconOnClickListener { }
    }

    private fun setXiconRegion() {
        binding.textlayoutVacancyRegion.endIconDrawable = AppCompatResources.getDrawable(
            requireContext(),
            R.drawable.close_ic
        )
        binding.textlayoutVacancyRegion.setEndIconOnClickListener {
            locationFilterViewModel.clearRegion()
            setForwardArrowRegion()
        }
    }
}
