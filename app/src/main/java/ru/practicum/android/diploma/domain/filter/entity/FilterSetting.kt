package ru.practicum.android.diploma.domain.filter.entity

sealed class FilterSetting {
    data class Area(
        val country: AreaEntity?,
        val region: AreaEntity?
    ) : FilterSetting()

    data class Industry(
        val id: String? = null,
        val name: String? = null
    ) : FilterSetting()

    data class Salary(
        val salary: Int? = null,
        val onlyWithSalary: Boolean? = null
    ) : FilterSetting()
}
