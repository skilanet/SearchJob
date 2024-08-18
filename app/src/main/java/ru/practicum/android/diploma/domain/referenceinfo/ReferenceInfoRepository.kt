package ru.practicum.android.diploma.domain.referenceinfo

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.referenceinfo.entity.RegionTreeResource

interface ReferenceInfoRepository {
    suspend fun getRegionsTree(id: String): Flow<RegionTreeResource>
}
