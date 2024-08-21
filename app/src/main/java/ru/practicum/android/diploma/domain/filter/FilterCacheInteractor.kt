package ru.practicum.android.diploma.domain.filter

import ru.practicum.android.diploma.domain.filter.entity.Filter
import ru.practicum.android.diploma.domain.filter.entity.FilterSetting

interface FilterCacheInteractor {
    fun createCache()
    fun getCache(): Filter?
    fun commitCache()
    fun isCachedFilterChanged(): Boolean
    fun isCachedFilterEmpty(): Boolean
    fun writeCache(setting: FilterSetting)
    fun invalidateCache()
}
