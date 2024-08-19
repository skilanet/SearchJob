package ru.practicum.android.diploma.presentation.filterregion.state

import ru.practicum.android.diploma.domain.filter.entity.AreaEntity

sealed class RegionFilterState {
    data object Error : RegionFilterState()
    data class Content(val regions: List<AreaEntity>) : RegionFilterState()
    data class Filter(val query: String) : RegionFilterState()
}
