package ru.practicum.android.diploma.domain.filter.impl

import ru.practicum.android.diploma.domain.filter.FilterCacheInteractor
import ru.practicum.android.diploma.domain.filter.FilterCacheRepository
import ru.practicum.android.diploma.domain.filter.entity.Filter
import ru.practicum.android.diploma.domain.filter.entity.FilterSetting

class FilterCacheInteractorImpl(private val filterRepository: FilterCacheRepository) : FilterCacheInteractor {
    override fun createCache() {
        filterRepository.createCache()
    }

    override fun getCache(): Filter? {
        return filterRepository.getCache()
    }

    override fun commitCache() {
        filterRepository.commitCache()
    }

    override fun isCachedFilterChanged(): Boolean {
        return filterRepository.isCachedFilterChanged()
    }

    override fun isCachedFilterEmpty(): Boolean {
        return filterRepository.isCachedFilterEmpty()
    }

    override fun writeCache(setting: FilterSetting) {
        filterRepository.writeCache(setting)
    }

    override fun invalidateCache() {
        filterRepository.invalidateCache()
    }
}
