package ru.practicum.android.diploma.domain.search.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.search.SearchInteractor
import ru.practicum.android.diploma.domain.search.SearchRepository
import ru.practicum.android.diploma.domain.search.entity.Resource

class SearchInteractorImpl(private val searchRepository: SearchRepository) : SearchInteractor {
    override suspend fun search(
        filter: Filter?,
        text: String,
        page: Int,
        perPage: Int
    ): Flow<Resource> {
        return searchRepository.search(
            filter = filter,
            text = text,
            page = page,
            perPage = perPage
        )
    }
}
