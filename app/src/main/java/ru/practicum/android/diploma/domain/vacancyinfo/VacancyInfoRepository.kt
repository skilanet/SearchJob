package ru.practicum.android.diploma.domain.vacancyinfo

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.VacancySearchResource

interface VacancyInfoRepository {
    suspend fun searchVacancy(id: String): Flow<VacancySearchResource>
}
