package ru.practicum.android.diploma.data.vacancyinfo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.data.dto.GetVacancyRequest
import ru.practicum.android.diploma.data.dto.GetVacancyResponse
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.models.ErrorCode
import ru.practicum.android.diploma.domain.models.VacancySearchResource
import ru.practicum.android.diploma.domain.models.VacancySearchResultType
import ru.practicum.android.diploma.domain.vacancyinfo.VacancyInfoRepository
import ru.practicum.android.diploma.util.mappers.VacancyMapper

class VacancyRepositoryImpl(
    private val networkClient: NetworkClient,
    private val vacancyMapper: VacancyMapper
) : VacancyInfoRepository {
    override suspend fun searchVacancy(id: String): Flow<VacancySearchResource> = flow {
        val request = GetVacancyRequest(id)
        val response = networkClient.doRequest(request) as GetVacancyResponse
        val resource = when (response.resultCode) {
            ErrorCode.SUCCESS -> VacancySearchResource(
                vacancyMapper.mapDtoToFullModel(response.data),
                VacancySearchResultType.SUCCESS
            )

            ErrorCode.NO_CONNECTION -> VacancySearchResource(null, VacancySearchResultType.NETWORK_ERROR)

            ErrorCode.BAD_REQUEST -> VacancySearchResource(null, VacancySearchResultType.NETWORK_ERROR)

            ErrorCode.NOT_FOUND -> VacancySearchResource(null, VacancySearchResultType.NOT_FOUND)

            else -> VacancySearchResource(null, VacancySearchResultType.NETWORK_ERROR)
        }
        emit(resource)
    }.flowOn(Dispatchers.IO)
}
