package ru.practicum.android.diploma.data.referenceinfo.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.data.dto.AreaParent
import ru.practicum.android.diploma.data.dto.AreasRequest
import ru.practicum.android.diploma.data.dto.AreasResponse
import ru.practicum.android.diploma.data.dto.CountriesRequest
import ru.practicum.android.diploma.data.dto.CountriesResponse
import ru.practicum.android.diploma.data.dto.IndustriesRequest
import ru.practicum.android.diploma.data.dto.IndustriesResponse
import ru.practicum.android.diploma.data.dto.IndustryParent
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.impl.RetrofitNetworkClient
import ru.practicum.android.diploma.domain.filter.entity.AreaEntity
import ru.practicum.android.diploma.domain.filter.entity.Resource
import ru.practicum.android.diploma.domain.models.ErrorCode
import ru.practicum.android.diploma.domain.referenceinfo.ReferenceInfoRepository
import ru.practicum.android.diploma.domain.referenceinfo.entity.IndustriesResource
import ru.practicum.android.diploma.domain.referenceinfo.entity.Industry
import ru.practicum.android.diploma.domain.referenceinfo.entity.RegionListResource
import ru.practicum.android.diploma.util.mappers.AreaMapper
import ru.practicum.android.diploma.util.mappers.IndustryMapper

class ReferenceInfoRepositoryImpl(
    private val networkClient: NetworkClient,
    private val areaMapper: AreaMapper,
    private val industryMapper: IndustryMapper
) : ReferenceInfoRepository {
    override suspend fun getRegionsList(id: String?): Flow<RegionListResource> = flow {
        val response = networkClient.doRequest(AreasRequest(id))
        when {
            response !is AreasResponse -> emit(RegionListResource(emptyList(), true))
            response.resultCode == ErrorCode.SUCCESS -> flattenTree(response.data, id == "1001").collect {
                emit(RegionListResource(it, false))
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
                ErrorCode.SUCCESS -> {
                    val data = response.data.map {
                        areaMapper.map(it)
                    }
                        .toMutableList()
                    val otherRegionsEntity = data.find { it.id == "1001" }
                    if (otherRegionsEntity != null) {
                        data.remove(otherRegionsEntity)
                        data.add(otherRegionsEntity)
                    }
                    Resource.Success(data)
                }
                ErrorCode.NOT_FOUND -> Resource.Error(ErrorCode.NOT_FOUND)
                ErrorCode.NO_CONNECTION -> Resource.Error(ErrorCode.NO_CONNECTION)
                else -> Resource.Error(ErrorCode.BAD_REQUEST)
            }
        }
        emit(resource)
    }.flowOn(Dispatchers.IO)
        .catch { Resource.Error(ErrorCode.BAD_REQUEST) }

    private fun flattenTree(
        regionTree: List<AreaParent>,
        otherRegions: Boolean
    ): Flow<List<AreaEntity>> = flow {
        val mutex = Mutex()
        val result = mutableListOf<AreaEntity>()
        suspend fun dfsParallel(
            area: AreaParent,
            parentCountry: AreaParent
        ) {
            withContext(Dispatchers.Default) {
                if (area.parentId != null && (area.parentId != "1001" || otherRegions)) {
                    mutex.withLock {
                        result.add(areaMapper.map(area, parentCountry))
                    }
                }
                val jobs = area.areas.map { child ->
                    async(Dispatchers.Default) { dfsParallel(child, parentCountry) }
                }
                jobs.awaitAll()
            }
        }

        val jobs = regionTree.map { parent ->
            withContext(Dispatchers.Default) {
                async(Dispatchers.Default) { dfsParallel(parent, parent) }
            }
        }
        jobs.awaitAll()
        result.sortBy { it.name }
        emit(result)
    }.flowOn(Dispatchers.Default)

    override suspend fun getIndustries(): Flow<IndustriesResource> {
        return flow {
            val response = networkClient.doRequest(IndustriesRequest)
            emit(
                if (response !is IndustriesResponse) {
                    IndustriesResource.Error(ErrorCode.BAD_REQUEST)
                } else {
                    when (response.resultCode) {
                        RetrofitNetworkClient.SUCCESS -> IndustriesResource.Success(parseIndustries(response.data))
                        RetrofitNetworkClient.NO_CONNECTION -> IndustriesResource.Error(ErrorCode.NO_CONNECTION)
                        RetrofitNetworkClient.NOT_FOUND -> IndustriesResource.Error(ErrorCode.NOT_FOUND)
                        else -> IndustriesResource.Error(RetrofitNetworkClient.BAD_REQUEST)
                    }
                }
            )
        }.flowOn(Dispatchers.IO)
    }

    private fun parseIndustries(industries: List<IndustryParent>): List<Industry> {
        val result = mutableSetOf<Industry>()
        fun parse(industry: IndustryParent) {
            for (i in industry.industries) {
                result.add(industryMapper.map(i))
            }
            result.add(industryMapper.map(industry))
        }
        industries.map(::parse)
        result.sortedWith(compareBy { it.name })
        return result.toList()
    }
}
