package ru.practicum.android.diploma.data.favorites

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.domain.favorites.FavoritesRepository
import ru.practicum.android.diploma.domain.models.VacancyFull
import ru.practicum.android.diploma.util.mappers.VacancyMapper

class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val vacancyMapper: VacancyMapper
) : FavoritesRepository {
    override suspend fun addToFavorites(vacancy: VacancyFull) {
        appDatabase.favoritesDao().insert(vacancyMapper.mapFullModelToEntity(vacancy))
    }

    override suspend fun removeFromFavorites(vacancyId: String) {
        appDatabase.favoritesDao().delete(vacancyId)
    }

    override fun getFavorites(): Flow<List<VacancyFull>> = flow {
        val favorites =
            appDatabase.favoritesDao().getAll().map { vacancy -> vacancyMapper.mapEntityToFullModel(vacancy) }
        emit(favorites)
    }.flowOn(Dispatchers.IO)

    override fun checkVacancyInFavorites(vacancyId: String): Flow<Boolean> = flow {
        emit(appDatabase.favoritesDao().isFavorite(vacancyId))
    }
}
