package ru.practicum.android.diploma.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface FavoritesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favoritesEntity: FavoritesEntity)

    @Query("DELETE FROM favorites WHERE id = :id")
    suspend fun delete(id: String)

    @Query("SELECT * FROM favorites ORDER BY insertion_timestamp")
    suspend fun getAll(): List<FavoritesEntity>

    @Query("SELECT * FROM favorites WHERE id = :id")
    suspend fun getFavoriteVacancyById(id: String): FavoritesEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE id = :id LIMIT 1)")
    suspend fun isFavorite(id: String): Boolean

    @Update(entity = FavoritesEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateFavoriteVacancy(favoritesEntity: FavoritesEntity)
}
