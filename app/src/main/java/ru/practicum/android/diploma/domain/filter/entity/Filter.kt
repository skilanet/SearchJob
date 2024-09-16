package ru.practicum.android.diploma.domain.filter.entity

class Filter(
    var area: FilterSetting.Area? = null,
    var salary: FilterSetting.Salary? = null,
    var industry: FilterSetting.Industry? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Filter) return false

        return area == other.area &&
            salary == other.salary &&
            industry == other.industry
    }

    override fun hashCode(): Int {
        var result = area?.hashCode() ?: 0
        result = 31 * result + (salary?.hashCode() ?: 0)
        result = 31 * result + (industry?.hashCode() ?: 0)
        return result
    }
}
