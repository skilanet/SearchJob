package ru.practicum.android.diploma.domain.favorites

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.DatabaseResource
import ru.practicum.android.diploma.domain.models.VacancyFull

interface FavoritesRepository {
    suspend fun addToFavorites(vacancy: VacancyFull)
    suspend fun removeFromFavorites(vacancyId: String)
    suspend fun updateFavoriteVacancy(vacancy: VacancyFull)
    fun getVacancyById(id: String): Flow<VacancyFull?>
    fun getFavorites(): Flow<DatabaseResource>
    fun checkVacancyInFavorites(vacancyId: String): Flow<Boolean>
}
