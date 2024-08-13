package ru.practicum.android.diploma.util.mappers

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.practicum.android.diploma.data.db.FavoritesEntity
import ru.practicum.android.diploma.data.dto.VacancyDto
import ru.practicum.android.diploma.data.dto.VacancyLightDto
import ru.practicum.android.diploma.domain.models.VacancyFull
import ru.practicum.android.diploma.domain.models.VacancyLight

class VacancyMapper(private val gson: Gson) {
    fun mapDtoToFullModel(dto: VacancyDto): VacancyFull {
        return VacancyFull(
            dto.id,
            dto.name.orEmpty(),
            dto.employer?.name.orEmpty(),
            dto.employer?.logoUrls?.logo90,
            dto.employer?.logoUrls?.logo240,
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

    fun map(dto: VacancyLightDto): VacancyLight {
        return VacancyLight(
            dto.id,
            dto.name.orEmpty(),
            dto.employer?.name.orEmpty(),
            dto.employer?.logoUrls?.logo90,
            dto.employer?.logoUrls?.logo240,
            dto.employer?.logoUrls?.original,
            dto.salary?.from,
            dto.salary?.to,
            dto.salary?.currency,
        )
    }

    fun mapFullModelToEntity(model: VacancyFull): FavoritesEntity {
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
            model.employment,
            model.schedule,
            model.experience,
            gson.toJson(model.keySkills),
            model.description
        )
    }

    fun mapEntityToFullModel(entity: FavoritesEntity): VacancyFull {
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
            gson.fromJson(
                entity.keySkills,
                object : TypeToken<List<String>>() {}.type
            ),
            entity.description.orEmpty()
        )
    }
    fun mapEntityToLightModel(entity: FavoritesEntity): VacancyLight {
        return VacancyLight(
            entity.id,
            entity.name.orEmpty(),
            entity.employerName.orEmpty(),
            entity.employerLogo90,
            entity.employerLogo240,
            entity.employerLogoOriginal,
            entity.salaryFrom,
            entity.salaryTo,
            entity.salaryCurrency
        )
    }
}
