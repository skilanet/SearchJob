package ru.practicum.android.diploma.domain.filter.entity

data class Filter(
    var area: FilterSetting.Area? = null,
    var salary: FilterSetting.Salary? = null,
    var industry: FilterSetting.Industry? = null
)
