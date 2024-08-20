package ru.practicum.android.diploma.domain.referenceinfo

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.referenceinfo.entity.IndustriesResource
import ru.practicum.android.diploma.domain.filter.entity.Resource
import ru.practicum.android.diploma.domain.referenceinfo.entity.RegionListResource

interface ReferenceInfoRepository {
    suspend fun getRegionsList(id: String?): Flow<RegionListResource>
    fun getCountries(): Flow<Resource>
    suspend fun getIndustries(): Flow<IndustriesResource>
}
