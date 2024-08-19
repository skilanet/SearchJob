package ru.practicum.android.diploma.data.referenceinfo.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.data.dto.AreaParent
import ru.practicum.android.diploma.data.dto.AreasRequest
import ru.practicum.android.diploma.data.dto.AreasResponse
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.filter.entity.AreaEntity
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

    private fun flattenTree(regionTree: List<AreaParent>): Flow<List<AreaEntity>> = flow {
        val result = mutableListOf<AreaEntity>()
        suspend fun dfsParallel(area: AreaParent, parentCountry: AreaParent) {
            withContext(Dispatchers.Default) {
                if (area.parentId != null) {
                    result.add(areaMapper.map(area, parentCountry))
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

}
