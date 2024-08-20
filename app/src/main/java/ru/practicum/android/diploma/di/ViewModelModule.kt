package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.presentation.favorites.FavoritesViewModel
import ru.practicum.android.diploma.presentation.filterregion.RegionFilterViewModel
import ru.practicum.android.diploma.presentation.vacancyinfo.VacancyInfoViewModel
import ru.practicum.android.diploma.ui.filterindustry.FilterIndustryViewModel
import ru.practicum.android.diploma.ui.search.SearchViewModel

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
        RegionFilterViewModel(get())
    }

    viewModel {
        FilterIndustryViewModel(
            get(),
            get()
        )
    }
}
