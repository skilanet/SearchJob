package ru.practicum.android.diploma.domain.search.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.filter.FilterInteractor
import ru.practicum.android.diploma.domain.search.SearchInteractor
import ru.practicum.android.diploma.domain.search.SearchRepository
import ru.practicum.android.diploma.domain.search.entity.Resource

class SearchInteractorImpl(
    private val searchRepository: SearchRepository,
    private val filterInteractor: FilterInteractor
) : SearchInteractor {
    override suspend fun search(
        text: String,
        page: Int,
        perPage: Int
    ): Flow<Resource> {
        val filter = filterInteractor.getFilter()
        return searchRepository.search(
            filter = filter,
            text = text,
            page = page,
            perPage = perPage
        )
    }
}
