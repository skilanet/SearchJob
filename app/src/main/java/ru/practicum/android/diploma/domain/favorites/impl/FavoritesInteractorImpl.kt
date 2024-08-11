package ru.practicum.android.diploma.domain.favorites.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.domain.favorites.FavoritesInteractor
import ru.practicum.android.diploma.domain.favorites.FavoritesRepository
import ru.practicum.android.diploma.domain.models.VacancyFull

class FavoritesInteractorImpl(val vacancyRepository: FavoritesRepository) : FavoritesInteractor {
    override suspend fun addToFavorites(vacancy: VacancyFull) {
        vacancyRepository.addToFavorites(vacancy)
    }

    override suspend fun removeFromFavorites(vacancyId: String) {
        vacancyRepository.removeFromFavorites(vacancyId)
    }

    override fun getFavorites(): Flow<List<VacancyFull>> {
        return vacancyRepository.getFavorites().map { list -> list.reversed() }
    }

    override fun checkVacancyInFavorites(vacancyId: String): Flow<Boolean> {
        return vacancyRepository.checkVacancyInFavorites(vacancyId)
    }

}
