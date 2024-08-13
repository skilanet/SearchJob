package ru.practicum.android.diploma.data.favorites

import android.database.SQLException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.domain.favorites.FavoritesRepository
import ru.practicum.android.diploma.domain.models.DatabaseResource
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

    override suspend fun updateFavoriteVacancy(vacancy: VacancyFull) {
        appDatabase.favoritesDao().updateFavoriteVacancy(vacancyMapper.mapFullModelToEntity(vacancy))
    }

    override fun getVacancyById(id: String): Flow<VacancyFull?> = flow<VacancyFull?> {
        val vacancy = appDatabase.favoritesDao().getFavoriteVacancyById(id)
        if (vacancy != null) {
            emit(vacancyMapper.mapEntityToFullModel(vacancy))
        } else {
            emit(null)
        }

    }.flowOn(Dispatchers.IO)

    override fun getFavorites(): Flow<DatabaseResource> = flow {
        try {
            val favorites =
                appDatabase.favoritesDao().getAll().map { vacancy -> vacancyMapper.mapEntityToLightModel(vacancy) }
            val resource = DatabaseResource(favorites, false)
            emit(resource)
        } catch (e: SQLException) {
            e.printStackTrace()
            emit(DatabaseResource(emptyList(), true))
        }

    }.flowOn(Dispatchers.IO)

    override fun checkVacancyInFavorites(vacancyId: String): Flow<Boolean> = flow {
        emit(appDatabase.favoritesDao().isFavorite(vacancyId))
    }

}
