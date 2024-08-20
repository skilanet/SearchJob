package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.data.favorites.FavoritesRepositoryImpl
import ru.practicum.android.diploma.data.filter.impl.FilterRepositoryImpl
import ru.practicum.android.diploma.data.referenceinfo.impl.ReferenceInfoRepositoryImpl
import ru.practicum.android.diploma.data.search.impl.SearchRepositoryImpl
import ru.practicum.android.diploma.data.vacancyinfo.VacancyRepositoryImpl
import ru.practicum.android.diploma.domain.favorites.FavoritesRepository
import ru.practicum.android.diploma.domain.filter.FilterRepository
import ru.practicum.android.diploma.domain.referenceinfo.ReferenceInfoRepository
import ru.practicum.android.diploma.domain.search.SearchRepository
import ru.practicum.android.diploma.domain.vacancyinfo.VacancyInfoRepository
import ru.practicum.android.diploma.util.mappers.FilterMapper
import ru.practicum.android.diploma.util.mappers.IndustryMapper
import ru.practicum.android.diploma.util.mappers.VacancyMapper

val repositoryModule = module {
    single<FavoritesRepository> {
        FavoritesRepositoryImpl(
            get(),
            get()
        )
    }
    single<SearchRepository> {
        SearchRepositoryImpl(
            get(),
            get(),
            get()
        )
    }
    single<VacancyInfoRepository> {
        VacancyRepositoryImpl(
            get(),
            get()
        )
    }
    single<FilterRepository> {
        FilterRepositoryImpl(
            get(filtersQualifier),
            get(),
        )
    }
    single<ReferenceInfoRepository> {
        ReferenceInfoRepositoryImpl(
            get(),
            get()
        )
    }

    single<ReferenceInfoRepository> {
        ReferenceInfoRepositoryImpl(
            get(),
            get()
        )
    }

    factory { VacancyMapper(get()) }
    factory { FilterMapper() }
    factory { IndustryMapper() }
}
