package ru.practicum.android.diploma.domain.referenceinfo

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.referenceinfo.entity.RegionListResource

interface ReferenceInfoRepository {
    suspend fun getRegionsList(id: String): Flow<RegionListResource>
}
