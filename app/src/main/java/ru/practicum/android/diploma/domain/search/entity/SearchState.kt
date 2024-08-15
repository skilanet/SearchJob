package ru.practicum.android.diploma.domain.search.entity

import androidx.paging.PagingData
import ru.practicum.android.diploma.domain.models.VacancyLight

sealed interface SearchState {
    data object Start : SearchState
    data class Content(val data: PagingData<VacancyLight>, val totalFound: Int?) : SearchState
    data object Loading : SearchState
    data class Error(val type: ErrorType) : SearchState

}
