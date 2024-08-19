package ru.practicum.android.diploma.util.mappers

import ru.practicum.android.diploma.data.dto.Area
import ru.practicum.android.diploma.data.dto.AreaParent
import ru.practicum.android.diploma.domain.filter.entity.AreaEntity

class AreaMapper {
    fun map(dto: AreaParent): AreaEntity {
        return with(dto) {
            AreaEntity(
                id = id,
                name = name
            )
        }
    }

    fun map(area: Area): AreaEntity {
        return with(area) {
            AreaEntity(
                id = id!!,
                name = name!!
            )
        }
    }
}
