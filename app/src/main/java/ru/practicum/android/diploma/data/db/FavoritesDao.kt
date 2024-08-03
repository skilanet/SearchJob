package ru.practicum.android.diploma.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoritesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favoritesEntity: FavoritesEntity)

    @Query("DELETE FROM favorites WHERE id = :id")
    suspend fun delete(id: String)

    @Query("SELECT * FROM favorites ")
    suspend fun getAll(): List<FavoritesEntity>
}
