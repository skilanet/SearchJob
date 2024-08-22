package ru.practicum.android.diploma.domain.search.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.practicum.android.diploma.data.paging.VacancyPagingSource
import ru.practicum.android.diploma.domain.filter.FilterInteractor
import ru.practicum.android.diploma.domain.models.VacancyLight
import ru.practicum.android.diploma.domain.search.SearchInteractor
import ru.practicum.android.diploma.domain.search.SearchRepository
import ru.practicum.android.diploma.domain.search.entity.Resource

class SearchInteractorImpl(
    private val searchRepository: SearchRepository,
    private val filterInteractor: FilterInteractor
) : SearchInteractor {
    private val totalFoundFlowInternal = MutableSharedFlow<Int?>()
    override val totalFoundFlow: Flow<Int?> = totalFoundFlowInternal.asSharedFlow()
    private var lastSearchedText = ""

    override suspend fun search(text: String): Flow<PagingData<VacancyLight>> {
        val filter = filterInteractor.getFilter()
        return Pager(
            config = PagingConfig(pageSize = VacancyPagingSource.PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = {
                VacancyPagingSource { page, perPage ->
                    val resource = searchRepository.search(filter, text, page, perPage)
                    if (resource is Resource.Success && text != lastSearchedText) {
                        totalFoundFlowInternal.emit(resource.total)
                    }
                    lastSearchedText = text
                    resource
                }
            }
        ).flow
    }
}
