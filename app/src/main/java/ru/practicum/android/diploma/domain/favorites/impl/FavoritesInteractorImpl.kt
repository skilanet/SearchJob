package ru.practicum.android.diploma.domain.favorites.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.favorites.FavoritesInteractor
import ru.practicum.android.diploma.domain.favorites.FavoritesRepository
import ru.practicum.android.diploma.domain.models.DatabaseResource
import ru.practicum.android.diploma.domain.models.VacancyFull

class FavoritesInteractorImpl(private val vacancyRepository: FavoritesRepository) : FavoritesInteractor {
    override suspend fun addToFavorites(vacancy: VacancyFull) {
        vacancyRepository.addToFavorites(vacancy)
    }

    override suspend fun removeFromFavorites(vacancyId: String) {
        vacancyRepository.removeFromFavorites(vacancyId)
    }

    override fun getFavorites(): Flow<DatabaseResource> {
        return vacancyRepository.getFavorites()
    }

    override fun checkVacancyInFavorites(vacancyId: String): Flow<Boolean> {
        return vacancyRepository.checkVacancyInFavorites(vacancyId)
    }

}
