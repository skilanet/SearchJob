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
                id = id,
                name = name.orEmpty(),
                employerName = employer?.name.orEmpty(),
                employerLogo90 = employer?.logoUrls?.logo90,
                employerLogo240 = employer?.logoUrls?.logo240,
                employerLogoOriginal = employer?.logoUrls?.original,
                salaryFrom = salary?.from,
                salaryTo = salary?.to,
                salaryCurrency = salary?.currency,
                area = area?.name,
                employment = employment?.name.orEmpty(),
                schedule = schedule?.name.orEmpty(),
                experience = experience?.name.orEmpty(),
                keySkills = keySkills.map { it.name.orEmpty() },
                description = description.orEmpty(),
                url = url,
                alternativeUrl = alternativeUrl
            )
        }
    }

    fun map(dto: VacancyLightDto): VacancyLight {
        return with(dto) {
            VacancyLight(
                id = id,
                name = name.orEmpty(),
                employerName = employer?.name.orEmpty(),
                employerLogo90 = employer?.logoUrls?.logo90,
                employerLogo240 = employer?.logoUrls?.logo240,
                employerLogoOriginal = employer?.logoUrls?.original,
                salaryFrom = salary?.from,
                salaryTo = salary?.to,
                salaryCurrency = salary?.currency,
            )
        }
    }

    fun mapFullModelToEntity(model: VacancyFull): FavoritesEntity {
        return with(model) {
            FavoritesEntity(
                id = id,
                name = name,
                employerName = employerName,
                employerLogo90 = employerLogo90,
                employerLogo240 = employerLogo240,
                employerLogoOriginal = employerLogoOriginal,
                salaryFrom = salaryFrom,
                salaryTo = salaryTo,
                salaryCurrency = salaryCurrency,
                area = area,
                employment = employment,
                schedule = schedule,
                experience = experience,
                keySkills = gson.toJson(keySkills),
                description = description,
                url = url,
                alternativeUrl = url
            )
        }
    }

    fun mapEntityToFullModel(entity: FavoritesEntity): VacancyFull {
        return with(entity) {
            VacancyFull(
                id = id,
                name = name.orEmpty(),
                employerName = employerName.orEmpty(),
                employerLogo90 = employerLogo90,
                employerLogo240 = employerLogo240,
                employerLogoOriginal = employerLogoOriginal,
                salaryFrom = salaryFrom,
                salaryTo = salaryTo,
                salaryCurrency = salaryCurrency,
                area = area,
                employment = employment.orEmpty(),
                schedule = schedule.orEmpty(),
                experience = experience.orEmpty(),
                keySkills = gson.fromJson(
                    keySkills,
                    object : TypeToken<List<String>>() {}.type
                ),
                description = description.orEmpty(),
                url = url,
                alternativeUrl = alternativeUrl
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
