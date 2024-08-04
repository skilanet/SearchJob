package ru.practicum.android.diploma.util.mappers

import ru.practicum.android.diploma.data.db.FavoritesEntity
import ru.practicum.android.diploma.data.dto.VacancyDto
import ru.practicum.android.diploma.domain.models.VacancyFull
import ru.practicum.android.diploma.domain.models.VacancyLight

class VacancyMapper {
    fun mapDtoToFullModel(dto: VacancyDto): VacancyFull {
        return VacancyFull(
            dto.id,
            dto.name.orEmpty(),
            dto.employer?.name.orEmpty(),
            dto.employer?.logoUrls?.employerLogo90,
            dto.employer?.logoUrls?.employerLogo240,
            dto.employer?.logoUrls?.original,
            dto.salary?.from,
            dto.salary?.to,
            dto.salary?.currency,
            dto.area?.name,
            dto.employment?.name.orEmpty(),
            dto.schedule?.name.orEmpty(),
            dto.experience?.name.orEmpty(),
            dto.keySkills.map { it.name.orEmpty() },
            dto.description.orEmpty()
        )
    }

    fun mapDtoToLightModel(dto: VacancyDto): VacancyLight {
        return VacancyLight(
            dto.id,
            dto.name.orEmpty(),
            dto.employer?.name.orEmpty(),
            dto.employer?.logoUrls?.employerLogo90,
            dto.employer?.logoUrls?.employerLogo240,
            dto.employer?.logoUrls?.original,
            dto.salary?.from,
            dto.salary?.to,
            dto.salary?.currency,
        )
    }

    fun mapFullModelToEntity(model: VacancyFull): FavoritesEntity {
        //пока оставляю поля где еще не определились с реализацией null
        return FavoritesEntity(
            model.id,
            model.name,
            model.employerName,
            model.employerLogo90,
            model.employerLogo240,
            model.employerLogoOriginal,
            model.salaryFrom,
            model.salaryTo,
            model.salaryCurrency,
            model.area,
            null,
            model.employment,
            model.schedule,
            model.experience,
            null,
            model.description
        )
    }

    fun mapEntityToFullModel(entity: FavoritesEntity): VacancyFull {
        //тут тоже пустой список пока что
        return VacancyFull(
            entity.id,
            entity.name.orEmpty(),
            entity.employerName.orEmpty(),
            entity.employerLogo90,
            entity.employerLogo240,
            entity.employerLogoOriginal,
            entity.salaryFrom,
            entity.salaryTo,
            entity.salaryCurrency,
            entity.area,
            entity.employment.orEmpty(),
            entity.schedule.orEmpty(),
            entity.experience.orEmpty(),
            emptyList<String>(),
            entity.description.orEmpty()
        )
    }
}
