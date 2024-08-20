package ru.practicum.android.diploma.domain.filterindustry.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.filterindustry.FilterIndustryInteractor
import ru.practicum.android.diploma.domain.referenceinfo.ReferenceInfoRepository
import ru.practicum.android.diploma.domain.referenceinfo.entity.IndustriesResource

class FilterIndustryInteractorImpl(private val referenceInfoRepository: ReferenceInfoRepository) :
    FilterIndustryInteractor {
    override suspend fun getIndustries(): Flow<IndustriesResource> {
        return referenceInfoRepository.getIndustries()
    }

}
