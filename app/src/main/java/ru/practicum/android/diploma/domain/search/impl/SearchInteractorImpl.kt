package ru.practicum.android.diploma.domain.search.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.paging.VacancyPagingSource
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.models.VacancyLight
import ru.practicum.android.diploma.domain.search.SearchInteractor
import ru.practicum.android.diploma.domain.search.SearchRepository

class SearchInteractorImpl(private val searchRepository: SearchRepository) : SearchInteractor {
    override suspend fun search(
        filter: Filter?,
        text: String
    ): Flow<PagingData<VacancyLight>> {
        return Pager(
            config = PagingConfig(VacancyPagingSource.PAGE_SIZE),
            pagingSourceFactory = {
                VacancyPagingSource { page, perPage ->
                    searchRepository.search(filter,text, page, perPage)
                }
            }
        ).flow
    }
}
