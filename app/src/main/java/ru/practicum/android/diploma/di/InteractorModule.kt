package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.favorites.FavoritesInteractor
import ru.practicum.android.diploma.domain.favorites.impl.FavoritesInteractorImpl

val interactorModule = module {
    factory<FavoritesInteractor> {
        FavoritesInteractorImpl(get())
    }
}
