package ru.practicum.android.diploma.domain.search

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.models.VacancyLight
import ru.practicum.android.diploma.domain.search.entity.Resource

interface SearchInteractor {
    val totalFoundFlow: StateFlow<Int?>
    suspend fun search(filter: Filter?, text: String): Flow<PagingData<VacancyLight>>
}
