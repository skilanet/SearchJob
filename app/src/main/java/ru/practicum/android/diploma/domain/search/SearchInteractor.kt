package ru.practicum.android.diploma.domain.search

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.search.entity.Resource

interface SearchInteractor {
    suspend fun search(text: String, page: Int): Flow<Resource>
}
