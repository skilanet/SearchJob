package ru.practicum.android.diploma.domain.search.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.domain.filter.FilterInteractor
import ru.practicum.android.diploma.domain.search.SearchInteractor
import ru.practicum.android.diploma.domain.search.SearchRepository
import ru.practicum.android.diploma.domain.search.entity.Resource

class SearchInteractorImpl(
    private val searchRepository: SearchRepository,
    private val filterInteractor: FilterInteractor
) : SearchInteractor {

    override suspend fun search(text: String, page: Int): Flow<Resource> = flow {
        val filter = filterInteractor.getFilter()
        val resource = searchRepository.search(filter, text, page, PAGE_SIZE)
        emit(resource)
    }.flowOn(Dispatchers.IO)

    companion object {
        const val PAGE_SIZE = 10
    }
}
