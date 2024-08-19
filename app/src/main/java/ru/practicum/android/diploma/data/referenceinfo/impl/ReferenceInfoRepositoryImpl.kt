package ru.practicum.android.diploma.data.referenceinfo.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.data.dto.AreaParent
import ru.practicum.android.diploma.data.dto.AreasRequest
import ru.practicum.android.diploma.data.dto.AreasResponse
import ru.practicum.android.diploma.data.dto.CountriesRequest
import ru.practicum.android.diploma.data.dto.CountriesResponse
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.filter.entity.AreaEntity
import ru.practicum.android.diploma.domain.filter.entity.Resource
import ru.practicum.android.diploma.domain.models.ErrorCode
import ru.practicum.android.diploma.domain.referenceinfo.ReferenceInfoRepository
import ru.practicum.android.diploma.domain.referenceinfo.entity.RegionListResource
import ru.practicum.android.diploma.util.mappers.AreaMapper

class ReferenceInfoRepositoryImpl(private val networkClient: NetworkClient, private val areaMapper: AreaMapper) :
    ReferenceInfoRepository {
    override suspend fun getRegionsList(id: String?): Flow<RegionListResource> = flow {
        val request = AreasRequest(id)
        val response = networkClient.doRequest(request) as AreasResponse
        when (response.resultCode) {
            ErrorCode.SUCCESS -> {
                flattenTree(response.data).collect {
                    emit(RegionListResource(it, false))
                }
            }
            else -> emit(RegionListResource(emptyList(), true))
        }
    }.flowOn(Dispatchers.IO)

    override fun getCountries(): Flow<Resource> = flow {
        val response = networkClient.doRequest(CountriesRequest)
        val resource = if (response !is CountriesResponse) {
            Resource.Error(ErrorCode.BAD_REQUEST)
        } else {
            when (response.resultCode) {
                ErrorCode.SUCCESS -> Resource.Success(response.data.map {
                    areaMapper.map(it)
                })
                ErrorCode.NOT_FOUND -> Resource.Error(ErrorCode.NOT_FOUND)
                ErrorCode.NO_CONNECTION -> Resource.Error(ErrorCode.NO_CONNECTION)
                else -> Resource.Error(ErrorCode.BAD_REQUEST)
            }
        }
        emit(resource)
    }.flowOn(Dispatchers.IO)
        .catch { Resource.Error(ErrorCode.BAD_REQUEST) }

    private fun flattenTree(regionTree: List<AreaParent>): Flow<List<AreaEntity>> = flow {
        val result = mutableListOf<AreaEntity>()
        suspend fun dfsParallel(area: AreaParent) {
            withContext(Dispatchers.Default) {
                result.add(areaMapper.map(area))
                val jobs = area.areas.map { child ->
                    async(Dispatchers.Default) { dfsParallel(child) }
                }
                jobs.awaitAll()
            }
        }

        val jobs = regionTree.map { parent ->
            withContext(Dispatchers.Default) {
                async(Dispatchers.Default) { dfsParallel(parent) }
            }
        }
        jobs.awaitAll()
        result.sortBy { it.name }
        emit(result)
    }.flowOn(Dispatchers.Default)

}
