package ru.practicum.android.diploma.domain.filter.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.filter.FilterInteractor
import ru.practicum.android.diploma.domain.filter.FilterRepository
import ru.practicum.android.diploma.domain.filter.entity.Filter
import ru.practicum.android.diploma.domain.filter.entity.FilterSetting
import ru.practicum.android.diploma.domain.filter.entity.Resource

class FilterInteractorImpl(private val filterRepository: FilterRepository) : FilterInteractor {
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
        return filterRepository.getCountries()
    }
}
