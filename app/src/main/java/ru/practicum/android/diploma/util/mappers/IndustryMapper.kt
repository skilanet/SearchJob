package ru.practicum.android.diploma.util.mappers

import ru.practicum.android.diploma.data.dto.IndustryDto
import ru.practicum.android.diploma.data.dto.IndustryParent
import ru.practicum.android.diploma.domain.referenceinfo.entity.Industry

class IndustryMapper {

    fun map(industryParent: IndustryParent): Industry {
        return Industry(
            id = industryParent.id,
            name = industryParent.name,
        )
    }

    fun map(industryDto: IndustryDto): Industry {
        return Industry(
            id = industryDto.id,
            name = industryDto.name,
        )
    }

}
