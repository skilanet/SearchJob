package ru.practicum.android.diploma.domain.search

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.VacancyLight

interface SearchInteractor {
    val totalFoundFlow: Flow<Int?>
    suspend fun search(text: String): Flow<PagingData<VacancyLight>>
}
