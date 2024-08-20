package ru.practicum.android.diploma.domain.filterindustry

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.referenceinfo.entity.IndustriesResource

interface FilterIndustryInteractor {
    suspend fun getIndustries(): Flow<IndustriesResource>
}
