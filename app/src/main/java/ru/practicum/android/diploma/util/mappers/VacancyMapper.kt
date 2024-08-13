package ru.practicum.android.diploma.util.mappers

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.practicum.android.diploma.data.db.FavoritesEntity
import ru.practicum.android.diploma.data.dto.VacancyDto
import ru.practicum.android.diploma.data.dto.VacancyLightDto
import ru.practicum.android.diploma.domain.models.VacancyFull
import ru.practicum.android.diploma.domain.models.VacancyLight

class VacancyMapper(
    private val gson: Gson
) {
    fun mapDtoToFullModel(dto: VacancyDto): VacancyFull {
        return with(dto) {
            VacancyFull(
                id,
                name.orEmpty(),
                employer?.name.orEmpty(),
                employer?.logoUrls?.logo90,
                employer?.logoUrls?.logo240,
                employer?.logoUrls?.original,
                salary?.from,
                salary?.to,
                salary?.currency,
                area?.name,
                employment?.name.orEmpty(),
                schedule?.name.orEmpty(),
                experience?.name.orEmpty(),
                keySkills.map { it.name.orEmpty() },
                description.orEmpty()
            )
        }
    }

    fun map(dto: VacancyLightDto): VacancyLight {
        return with(dto){
            VacancyLight(
                id,
                name.orEmpty(),
                employer?.name.orEmpty(),
                employer?.logoUrls?.logo90,
                employer?.logoUrls?.logo240,
                employer?.logoUrls?.original,
                salary?.from,
                salary?.to,
                salary?.currency,
            )
        }
    }

    fun mapFullModelToEntity(model: VacancyFull): FavoritesEntity {
        return with(model) {
            FavoritesEntity(
                id,
                name,
                employerName,
                employerLogo90,
                employerLogo240,
                employerLogoOriginal,
                salaryFrom,
                salaryTo,
                salaryCurrency,
                area,
                employment,
                schedule,
                experience,
                gson.toJson(keySkills),
                description
            )
        }
    }

    fun mapEntityToFullModel(entity: FavoritesEntity): VacancyFull {
        return with(entity) {
            VacancyFull(
                id,
                name.orEmpty(),
                employerName.orEmpty(),
                employerLogo90,
                employerLogo240,
                employerLogoOriginal,
                salaryFrom,
                salaryTo,
                salaryCurrency,
                area,
                employment.orEmpty(),
                schedule.orEmpty(),
                experience.orEmpty(),
                gson.fromJson(
                    keySkills,
                    object : TypeToken<List<String>>() {}.type
                ),
                description.orEmpty()
            )
        }
    }
    fun mapEntityToLightModel(entity: FavoritesEntity): VacancyLight {
        return with(entity) {
            VacancyLight(
                id,
                name.orEmpty(),
                employerName.orEmpty(),
                employerLogo90,
                employerLogo240,
                employerLogoOriginal,
                salaryFrom,
                salaryTo,
                salaryCurrency
            )
        }
    }
}
