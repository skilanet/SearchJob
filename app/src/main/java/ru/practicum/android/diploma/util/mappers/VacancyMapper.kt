package ru.practicum.android.diploma.util.mappers

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.data.db.FavoritesEntity
import ru.practicum.android.diploma.data.dto.VacancyDto
import ru.practicum.android.diploma.domain.models.VacancyFull
import ru.practicum.android.diploma.domain.models.VacancyLight

class VacancyMapper(
    private val context: Context,
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
                resultSalary = generateResultSalary(
                    salaryFrom = salary?.from,
                    salaryTo = salary?.to,
                    salaryCurrency = salary?.currency ?: ""
                ),
                area = area?.name,
                employment = employment?.name.orEmpty(),
                schedule = schedule?.name.orEmpty(),
                experience = experience?.name.orEmpty(),
                keySkills = keySkills.map { it.name.orEmpty() },
                description = description.orEmpty()
            )
        }
    }

    fun mapDtoToLightModel(dto: VacancyDto): VacancyLight {
        return with(dto) {
            VacancyLight(
                id = id,
                name = name.orEmpty(),
                employerName = employer?.name.orEmpty(),
                employerLogo90 = employer?.logoUrls?.logo90,
                employerLogo240 = employer?.logoUrls?.logo240,
                employerLogoOriginal = employer?.logoUrls?.original,
                resultSalary = generateResultSalary(
                    salaryFrom = salary?.from,
                    salaryTo = salary?.to,
                    salaryCurrency = salary?.currency ?: ""
                )
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
                resultSalary = resultSalary,
                area = area,
                employment = employment,
                schedule = schedule,
                experience = experience,
                keySkills = gson.toJson(keySkills),
                description = description
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
                resultSalary = resultSalary,
                area = area,
                employment = employment.orEmpty(),
                schedule = schedule.orEmpty(),
                experience = experience.orEmpty(),
                keySkills = gson.fromJson(
                    keySkills,
                    object : TypeToken<List<String>>() {}.type
                ),
                description = description.orEmpty()
            )
        }
    }

    private fun generateResultSalary(
        salaryFrom: Int?,
        salaryTo: Int?,
        salaryCurrency: String
    ): String {
        return if (salaryFrom == null && salaryTo == null) {
            context.resources.getString(R.string.empty_salary)
        } else if (salaryFrom == null) {
            context.resources.getString(
                R.string.max_salary,
                salaryTo,
                salaryCurrency
            )
        } else if (salaryTo == null) {
            context.resources.getString(
                R.string.min_salary,
                salaryFrom,
                salaryCurrency
            )
        } else {
            context.resources.getString(
                R.string.full_salary,
                salaryFrom,
                salaryCurrency,
                salaryTo
            )
        }
    }
}
