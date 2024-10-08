package ru.practicum.android.diploma.domain.filter

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.filter.entity.Filter
import ru.practicum.android.diploma.domain.filter.entity.FilterSetting
import ru.practicum.android.diploma.domain.filter.entity.Resource
import ru.practicum.android.diploma.domain.referenceinfo.entity.RegionListResource

interface FilterInteractor {
    fun initializeEmptyFilter()
    fun saveSetting(setting: FilterSetting)
    fun getFilter(): Filter?
    fun isFilterPresent(): Boolean
    fun getCountries(): Flow<Resource>
    suspend fun getRegionsList(id: String?): Flow<RegionListResource>
    fun deleteFilters()
    fun saveFilterApplicationSetting(apply: Boolean)
    fun readFilterApplicationSetting(): Boolean
}
