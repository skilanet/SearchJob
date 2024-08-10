package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.data.favorites.FavoritesRepositoryImpl
import ru.practicum.android.diploma.domain.favorites.FavoritesRepository

val repositoryModule = module {
    single<FavoritesRepository> {
        FavoritesRepositoryImpl(get(), get())
    }
}
