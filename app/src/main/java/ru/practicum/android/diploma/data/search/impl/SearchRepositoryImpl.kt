package ru.practicum.android.diploma.data.search.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.data.dto.VacanciesSearchRequest
import ru.practicum.android.diploma.data.dto.VacanciesSearchResponse
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.impl.RetrofitNetworkClient
import ru.practicum.android.diploma.domain.models.ErrorCode
import ru.practicum.android.diploma.domain.models.Filter
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
    ): Flow<Resource> {
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

        return flow {
            val response = networkClient.doRequest(req)
            val resource = if (response !is VacanciesSearchResponse) {
                Resource.Error(ErrorCode.BAD_REQUEST)
            } else {
                when (response.resultCode) {
                    RetrofitNetworkClient.SUCCESS -> Resource.Success(response.items.map {
                        vacancyMapper.map(it)
                    })

                    RetrofitNetworkClient.NO_CONNECTION -> Resource.Error(ErrorCode.NO_CONNECTION)
                    RetrofitNetworkClient.BAD_REQUEST -> Resource.Error(ErrorCode.BAD_REQUEST)
                    RetrofitNetworkClient.NOT_FOUND -> Resource.Error(ErrorCode.NOT_FOUND)
                    else -> Resource.Error(RetrofitNetworkClient.BAD_REQUEST)
                }

            }
            emit(resource)
        }.flowOn(Dispatchers.IO)

    }

}
