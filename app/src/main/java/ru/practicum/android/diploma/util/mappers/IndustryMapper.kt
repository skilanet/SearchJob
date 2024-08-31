package ru.practicum.android.diploma.util.mappers

import ru.practicum.android.diploma.data.dto.IndustryParent
import ru.practicum.android.diploma.domain.referenceinfo.entity.Industry

class IndustryMapper {

    fun map(industryDto: IndustryParent): Industry {
        return Industry(
            id = industryDto.id,
            name = industryDto.name,
        )

    }

}
