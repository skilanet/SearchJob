package ru.practicum.android.diploma.domain.referenceinfo

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.referenceinfo.entity.IndustriesResource

interface ReferenceInfoRepository {
    suspend fun getIndustries(): Flow<IndustriesResource>

}
