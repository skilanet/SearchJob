package ru.practicum.android.diploma.domain.vacancyinfo

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.VacancyInfoResource

interface VacancyInfoInteractor {
    suspend fun loadVacancy(id: String): Flow<VacancyInfoResource>
}
