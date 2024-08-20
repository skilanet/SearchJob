package ru.practicum.android.diploma.ui.filterlocation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
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
        binding.edittextVacancyRegion.setOnClickListener {
            findNavController().navigate(R.id.action_filterLocationFragment_to_filterRegionFragment)
        }
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onResume() {
        super.onResume()
        locationFilterViewModel.getSavedLocationFilterSettings()
    }

    private fun renderState(state: LocationState) {
        if (state.country != null) {
            binding.edittextVacancyCountry.setText(state.country.name)
            setXiconCountry()
        } else {
            binding.edittextVacancyCountry.setText("")
        }
        if (state.region != null) {
            binding.edittextVacancyRegion.setText(state.region.name)
            setXiconRegion()
        } else {
            binding.edittextVacancyRegion.setText("")
        }
    }

    private fun setForwardArrowCountry() {
        binding.textlayoutVacancyCountry.endIconDrawable = AppCompatResources.getDrawable(
            requireContext(),
            R.drawable.arrow_forward_ic
        )
        binding.textlayoutVacancyCountry.setEndIconOnClickListener { Unit }
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
        binding.textlayoutVacancyRegion.setEndIconOnClickListener { Unit }
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
