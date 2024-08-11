package ru.practicum.android.diploma.data.favorites

import android.database.SQLException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.domain.favorites.FavoritesRepository
import ru.practicum.android.diploma.domain.models.Resource
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

    override fun getFavorites(): Flow<Resource> = flow {
        try {
            val favorites =
                appDatabase.favoritesDao().getAll().map { vacancy -> vacancyMapper.mapEntityToLightModel(vacancy) }
            val resource = Resource.Success(favorites)
            emit(resource)
        } catch (e: SQLException) {
            e.printStackTrace()
            emit(Resource.Error(DATABASE_ERROR))
        }

    }.flowOn(Dispatchers.IO)

    override fun checkVacancyInFavorites(vacancyId: String): Flow<Boolean> = flow {
        emit(appDatabase.favoritesDao().isFavorite(vacancyId))
    }

    companion object {
        const val DATABASE_ERROR = -2
    }
}
