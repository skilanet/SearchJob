package ru.practicum.android.diploma.domain.filterindustry.entity

import ru.practicum.android.diploma.domain.referenceinfo.entity.Industry

data class FilterIndustryListState(
    val industries: List<Industry>,
    val current: Industry?
)
