package ru.practicum.android.diploma.domain.filter

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.filter.entity.Filter
import ru.practicum.android.diploma.domain.filter.entity.FilterSetting
import ru.practicum.android.diploma.domain.filter.entity.Resource

interface FilterRepository {
    fun saveSetting(setting: FilterSetting)
    fun getFilter(): Filter?
    fun isFilterPresent(): Boolean
    fun getCountries(): Flow<Resource>
}
