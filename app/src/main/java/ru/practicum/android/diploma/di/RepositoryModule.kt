package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.data.favorites.FavoritesRepositoryImpl
import ru.practicum.android.diploma.data.search.impl.SearchRepositoryImpl
import ru.practicum.android.diploma.domain.favorites.FavoritesRepository
import ru.practicum.android.diploma.domain.search.SearchRepository
import ru.practicum.android.diploma.util.mappers.FilterMapper

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

    factory { FilterMapper() }
}
