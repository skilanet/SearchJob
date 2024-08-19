package ru.practicum.android.diploma.util.mappers

import ru.practicum.android.diploma.data.dto.AreaParent
import ru.practicum.android.diploma.domain.filter.entity.AreaEntity

class AreaMapper {
    fun map(dto: AreaParent, parent: AreaParent): AreaEntity {
        return AreaEntity(
            dto.id,
            dto.name,
            AreaEntity(
                parent.id,
                parent.name
            )
        )
    }
}
