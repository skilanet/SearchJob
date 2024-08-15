package ru.practicum.android.diploma.domain.search.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.practicum.android.diploma.data.paging.VacancyPagingSource
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.models.VacancyLight
import ru.practicum.android.diploma.domain.search.SearchInteractor
import ru.practicum.android.diploma.domain.search.SearchRepository
import ru.practicum.android.diploma.domain.search.entity.Resource

class SearchInteractorImpl(private val searchRepository: SearchRepository) : SearchInteractor {
    private val totalFoundFlowInternal = MutableStateFlow<Int?>(null)
    override val totalFoundFlow: StateFlow<Int?> = totalFoundFlowInternal.asStateFlow()

    override suspend fun search(
        filter: Filter?,
        text: String
    ): Flow<PagingData<VacancyLight>> {
        return Pager(
            config = PagingConfig(VacancyPagingSource.PAGE_SIZE),
            pagingSourceFactory = {
                VacancyPagingSource { page, perPage ->
                    val resource = searchRepository.search(filter, text, page, perPage)
                    if (resource is Resource.Success) {
                        totalFoundFlowInternal.value = resource.total
                    }
                    resource
                }
            }
        ).flow
    }
}
