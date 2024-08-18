package ru.practicum.android.diploma.domain.filter.impl

import ru.practicum.android.diploma.domain.filter.FilterInteractor
import ru.practicum.android.diploma.domain.filter.FilterRepository
import ru.practicum.android.diploma.domain.filter.entity.Filter
import ru.practicum.android.diploma.domain.filter.entity.FilterSetting

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
}
