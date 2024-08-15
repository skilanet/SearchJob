package ru.practicum.android.diploma.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.practicum.android.diploma.domain.models.ErrorCode
import ru.practicum.android.diploma.domain.models.VacancyLight
import ru.practicum.android.diploma.domain.search.entity.Resource

class VacancyPagingSource(
    private val doRequest: suspend (page: Int, perPage: Int) -> Resource
) : PagingSource<Int, VacancyLight>() {
    override fun getRefreshKey(state: PagingState<Int, VacancyLight>) = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, VacancyLight> {
        val page = params.key ?: 0
        val resource = doRequest.invoke(page, PAGE_SIZE)
        return when (resource) {
            is Resource.Success -> {
                val prevKey = if (page == 0) null else page - 1
                val nextKey = if (resource.page == resource.pages) null else page + 1
                LoadResult.Page(resource.data, prevKey, nextKey)
            }

            is Resource.Error -> {
                val exception = when (resource.code) {
                    ErrorCode.NO_CONNECTION -> ConnectionException()
                    ErrorCode.BAD_REQUEST -> BadRequestException()
                    else -> IllegalStateException()
                }
                LoadResult.Error(exception)
            }
        }
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}
