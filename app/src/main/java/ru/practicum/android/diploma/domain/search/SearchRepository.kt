package ru.practicum.android.diploma.domain.search

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.models.Resource

interface SearchRepository {
    suspend fun search(
        filter: Filter?,
        page: Int,
        perPage: Int
    ): Flow<Resource>
}
