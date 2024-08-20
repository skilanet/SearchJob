package ru.practicum.android.diploma.ui.filtercountry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentFilterCountryBinding
import ru.practicum.android.diploma.presentation.filtercountry.CountryFilterViewModel
import ru.practicum.android.diploma.ui.filterregion.adapters.RegionListAdapter
import ru.practicum.android.diploma.util.BindingFragment

class FilterCountryFragment : BindingFragment<FragmentFilterCountryBinding>() {
    private val countryViewModel: CountryFilterViewModel by viewModel()
    private val adapter by lazy {
        RegionListAdapter { areaEntity ->
            countryViewModel.addCountryToFilter(areaEntity)
        }
    }

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentFilterCountryBinding {
        return FragmentFilterCountryBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerRegions.adapter = adapter

        countryViewModel.observeScreenStateLiveData().observe(viewLifecycleOwner) {
            adapter.setItems(it.countries)
        }

        countryViewModel.observeCountryAddedEvent().observe(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}
