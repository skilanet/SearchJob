package ru.practicum.android.diploma.domain.search

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.models.VacancyLight
import ru.practicum.android.diploma.domain.search.entity.Resource

interface SearchInteractor {
    suspend fun search(filter: Filter?, text: String): Flow<PagingData<VacancyLight>>
}
