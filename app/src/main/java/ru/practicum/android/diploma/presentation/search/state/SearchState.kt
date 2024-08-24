package ru.practicum.android.diploma.presentation.search.state

import ru.practicum.android.diploma.domain.models.VacancyLight
import ru.practicum.android.diploma.domain.search.entity.ErrorType

sealed interface SearchState {
    data object Start : SearchState
    data class Content(val data: List<VacancyLight>) : SearchState
    data object Loading : SearchState
    data object PageLoading : SearchState
    data class Error(val type: ErrorType) : SearchState

}
