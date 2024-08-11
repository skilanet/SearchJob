package ru.practicum.android.diploma.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.data.favorites.FavoritesRepositoryImpl
import ru.practicum.android.diploma.data.search.impl.SearchRepositoryImpl
import ru.practicum.android.diploma.domain.favorites.FavoritesRepository
import ru.practicum.android.diploma.domain.search.SearchRepository
import ru.practicum.android.diploma.util.mappers.FilterMapper
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

    factory { VacancyMapper(androidContext(), get()) }

    factory { FilterMapper() }
}
