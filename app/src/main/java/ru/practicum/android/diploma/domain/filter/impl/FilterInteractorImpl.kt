package ru.practicum.android.diploma.domain.filter.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.filter.FilterInteractor
import ru.practicum.android.diploma.domain.filter.FilterRepository
import ru.practicum.android.diploma.domain.filter.entity.Filter
import ru.practicum.android.diploma.domain.filter.entity.FilterSetting
import ru.practicum.android.diploma.domain.filter.entity.Resource
import ru.practicum.android.diploma.domain.referenceinfo.ReferenceInfoRepository
import ru.practicum.android.diploma.domain.referenceinfo.entity.RegionListResource

class FilterInteractorImpl(
    private val filterRepository: FilterRepository,
    private val referenceInfoRepository: ReferenceInfoRepository
) : FilterInteractor {
    override fun saveSetting(setting: FilterSetting) {
        filterRepository.saveSetting(setting)
    }

    override fun getFilter(): Filter? {
        return filterRepository.getFilter()
    }

    override fun isFilterPresent(): Boolean {
        return filterRepository.isFilterPresent()
    }

    override fun getCountries(): Flow<Resource> {
        return referenceInfoRepository.getCountries()
    }

    override suspend fun getRegionsList(id: String?): Flow<RegionListResource> {
        return referenceInfoRepository.getRegionsList(id)
    }
}
