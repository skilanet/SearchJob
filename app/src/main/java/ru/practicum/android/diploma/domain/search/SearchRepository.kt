package ru.practicum.android.diploma.domain.search

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.search.entity.Resource

interface SearchRepository {
    suspend fun search(
        filter: Filter?,
        text: String,
        page: Int,
        perPage: Int
    ): Flow<Resource>
}
