package ru.practicum.android.diploma.util.mappers

import ru.practicum.android.diploma.data.dto.FilterDto
import ru.practicum.android.diploma.domain.filter.entity.Filter

class FilterMapper {
    fun map(filter: Filter): FilterDto {
        return FilterDto(
            area = filter.area?.region?.id ?: filter.area?.country?.id,
            industry = filter.industry?.id,
            salary = filter.salary?.salary,
            onlyWithSalary = filter.salary?.onlyWithSalary
        )
    }
}
