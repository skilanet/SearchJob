package ru.practicum.android.diploma.presentation.favorites.state

import ru.practicum.android.diploma.domain.models.VacancyLight

sealed class FavoritesState {
    data object Empty: FavoritesState()
    data object Error: FavoritesState()
    data class Content(val vacancies: List<VacancyLight>): FavoritesState()
}
