package ru.practicum.android.diploma.util.mappers

import ru.practicum.android.diploma.data.dto.FilterDto
import ru.practicum.android.diploma.domain.models.Filter

class FilterMapper {
    fun map(filterDto: FilterDto): Filter {
        return Filter(
            area = filterDto.area,
            industry = filterDto.industry,
            salary = filterDto.salary,
            onlyWithSalary = filterDto.onlyWithSalary
        )

    }

    fun map(filter: Filter): FilterDto {
        return FilterDto(
            area = filter.area,
            industry = filter.industry,
            salary = filter.salary,
            onlyWithSalary = filter.onlyWithSalary
        )

    }
}
