package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.presentation.favorites.FavoritesViewModel
import ru.practicum.android.diploma.presentation.filtercountry.CountryFilterViewModel
import ru.practicum.android.diploma.presentation.filterlocation.LocationFilterViewModel
import ru.practicum.android.diploma.presentation.filterregion.RegionFilterViewModel
import ru.practicum.android.diploma.presentation.filtersettings.FilterSettingsViewModel
import ru.practicum.android.diploma.presentation.search.SearchViewModel
import ru.practicum.android.diploma.presentation.vacancyinfo.VacancyInfoViewModel
import ru.practicum.android.diploma.ui.filterindustry.FilterIndustryViewModel

val viewModelModule = module {
    viewModel<FavoritesViewModel> {
        FavoritesViewModel(get())
    }
    viewModel<VacancyInfoViewModel> {
        VacancyInfoViewModel(
            get(),
            get()
        )
    }
    viewModel {
        SearchViewModel(get(), get())
    }
    viewModel<RegionFilterViewModel> {
        RegionFilterViewModel(get(), get())
    }
    viewModel {
        FilterIndustryViewModel(
            get(),
            get()
        )
    }
    viewModel<LocationFilterViewModel> {
        LocationFilterViewModel(get(), get())
    }
    viewModel<CountryFilterViewModel> {
        CountryFilterViewModel(get(), get())
    }
    viewModel<FilterSettingsViewModel> {
        FilterSettingsViewModel(get(), get())
    }
}
