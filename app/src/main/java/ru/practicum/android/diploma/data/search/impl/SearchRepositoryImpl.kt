package ru.practicum.android.diploma.data.search.impl

import android.util.Log
import ru.practicum.android.diploma.data.dto.VacanciesSearchRequest
import ru.practicum.android.diploma.data.dto.VacanciesSearchResponse
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.impl.RetrofitNetworkClient
import ru.practicum.android.diploma.domain.filter.entity.Filter
import ru.practicum.android.diploma.domain.models.ErrorCode
import ru.practicum.android.diploma.domain.search.SearchRepository
import ru.practicum.android.diploma.domain.search.entity.Resource
import ru.practicum.android.diploma.util.mappers.FilterMapper
import ru.practicum.android.diploma.util.mappers.VacancyMapper

class SearchRepositoryImpl(
    private val networkClient: NetworkClient,
    private val filterMapper: FilterMapper,
    private val vacancyMapper: VacancyMapper
) : SearchRepository {
    override suspend fun search(
        filter: Filter?,
        text: String,
        page: Int,
        perPage: Int
    ): Resource {
        val filterDto = if (filter != null) {
            filterMapper.map(filter)
        } else {
            null
        }
        val req = VacanciesSearchRequest(
            filterDto = filterDto,
            text = text,
            page = page,
            perPage = perPage
        )

        val response = networkClient.doRequest(req)
        Log.i("response code", response.resultCode.toString())
        val resource = if (response !is VacanciesSearchResponse) {
            when (response.resultCode) {
                ErrorCode.NO_CONNECTION -> Resource.Error(ErrorCode.NO_CONNECTION)
                else -> Resource.Error(ErrorCode.BAD_REQUEST)
            }
        } else {
            when (response.resultCode) {
                RetrofitNetworkClient.SUCCESS -> Resource.Success(
                    response.items.map { vacancyMapper.map(it) },
                    response.page,
                    response.pages,
                    response.found
                )

                ErrorCode.NO_CONNECTION -> Resource.Error(ErrorCode.NO_CONNECTION)
                ErrorCode.BAD_REQUEST -> Resource.Error(ErrorCode.BAD_REQUEST)
                ErrorCode.NOT_FOUND -> Resource.Error(ErrorCode.NOT_FOUND)
                else -> Resource.Error(ErrorCode.BAD_REQUEST)
            }

        }
        return resource
    }

}
