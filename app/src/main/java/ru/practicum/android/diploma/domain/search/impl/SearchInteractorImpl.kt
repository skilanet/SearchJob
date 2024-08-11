package ru.practicum.android.diploma.domain.search.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.search.SearchInteractor
import ru.practicum.android.diploma.domain.search.SearchRepository

class SearchInteractorImpl(private val searchRepository: SearchRepository) : SearchInteractor {
    override suspend fun search(
        filter: Filter?,
        page: Int,
        perPage: Int
    ): Flow<Resource> {
        return searchRepository.search(
            filter = filter,
            page = page,
            perPage = perPage
        )
    }
}
