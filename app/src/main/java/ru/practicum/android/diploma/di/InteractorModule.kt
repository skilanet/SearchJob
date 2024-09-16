package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.favorites.FavoritesInteractor
import ru.practicum.android.diploma.domain.favorites.impl.FavoritesInteractorImpl
import ru.practicum.android.diploma.domain.filter.FilterCacheInteractor
import ru.practicum.android.diploma.domain.filter.FilterInteractor
import ru.practicum.android.diploma.domain.filter.impl.FilterCacheInteractorImpl
import ru.practicum.android.diploma.domain.filter.impl.FilterInteractorImpl
import ru.practicum.android.diploma.domain.filterindustry.FilterIndustryInteractor
import ru.practicum.android.diploma.domain.filterindustry.impl.FilterIndustryInteractorImpl
import ru.practicum.android.diploma.domain.search.SearchInteractor
import ru.practicum.android.diploma.domain.search.impl.SearchInteractorImpl
import ru.practicum.android.diploma.domain.vacancyinfo.VacancyInfoInteractor
import ru.practicum.android.diploma.domain.vacancyinfo.impl.VacancyInteractorImpl

val interactorModule = module {
    factory<FavoritesInteractor> {
        FavoritesInteractorImpl(get())
    }

    factory<SearchInteractor> {
        SearchInteractorImpl(
            get(),
            get()
        )
    }

    factory<VacancyInfoInteractor> {
        VacancyInteractorImpl(
            get(),
            get()
        )
    }

    factory<FilterInteractor> {
        FilterInteractorImpl(get(), get())
    }

    factory<FilterIndustryInteractor> {
        FilterIndustryInteractorImpl(get())
    }

    factory<FilterCacheInteractor> {
        FilterCacheInteractorImpl(get())
    }
}
