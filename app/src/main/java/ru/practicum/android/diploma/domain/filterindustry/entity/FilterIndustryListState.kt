package ru.practicum.android.diploma.domain.filterindustry.entity

import ru.practicum.android.diploma.domain.referenceinfo.entity.Industry

sealed interface FilterIndustryListState {
    data class Content(
        val industries: List<Industry>,
        val current: Industry?
    ) : FilterIndustryListState

    data object Error : FilterIndustryListState
}
