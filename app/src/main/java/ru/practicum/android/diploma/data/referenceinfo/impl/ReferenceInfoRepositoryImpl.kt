package ru.practicum.android.diploma.data.referenceinfo.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.data.dto.IndustriesRequest
import ru.practicum.android.diploma.data.dto.IndustriesResponse
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.impl.RetrofitNetworkClient
import ru.practicum.android.diploma.domain.models.ErrorCode
import ru.practicum.android.diploma.domain.referenceinfo.ReferenceInfoRepository
import ru.practicum.android.diploma.domain.referenceinfo.entity.IndustriesResource
import ru.practicum.android.diploma.util.mappers.IndustryMapper

class ReferenceInfoRepositoryImpl(
    private val networkClient: NetworkClient,
    private val industryMapper: IndustryMapper
) : ReferenceInfoRepository {
    override suspend fun getIndustries(): Flow<IndustriesResource> {
        return flow {
            val response = networkClient.doRequest(IndustriesRequest)
            val resource = if (response !is IndustriesResponse) {
                IndustriesResource.Error(ErrorCode.BAD_REQUEST)
            } else {
                when (response.resultCode) {
                    RetrofitNetworkClient.SUCCESS -> IndustriesResource.Success(response.data.map {
                        industryMapper.map(it)
                    })

                    RetrofitNetworkClient.NO_CONNECTION -> IndustriesResource.Error(ErrorCode.NO_CONNECTION)
                    RetrofitNetworkClient.BAD_REQUEST -> IndustriesResource.Error(ErrorCode.BAD_REQUEST)
                    RetrofitNetworkClient.NOT_FOUND -> IndustriesResource.Error(ErrorCode.NOT_FOUND)
                    else -> IndustriesResource.Error(RetrofitNetworkClient.BAD_REQUEST)
                }
            }
            emit(resource)
        }.flowOn(Dispatchers.IO).catch { emit(IndustriesResource.Error(ErrorCode.BAD_REQUEST)) }

    }
}
