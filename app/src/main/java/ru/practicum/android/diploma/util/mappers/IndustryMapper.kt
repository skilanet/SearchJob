package ru.practicum.android.diploma.util.mappers

import ru.practicum.android.diploma.data.dto.IndustryParent
import ru.practicum.android.diploma.domain.referenceinfo.entity.Industry

class IndustryMapper {

    fun map(industryDto: IndustryParent): Industry {
        val industries = industryDto.industries.map {
            Industry(
                it.id,
                it.name
            )
        }
        return Industry(
            industryDto.id,
            industryDto.name,
            industries
        )

    }

}
