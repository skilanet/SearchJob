package ru.practicum.android.diploma.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoritesEntity(
    @PrimaryKey val id: String,
    val name: String?,
    val employerName: String?,
    val employerLogo90: String?,
    val employerLogo240: String?,
    val employerLogoOriginal: String?,
    val resultSalary: String,
    val area: String?,
    val employment: String?,
    val schedule: String?,
    val experience: String?,
    val keySkills: String?,
    val description: String?,
    @ColumnInfo(name = "insertion_timestamp")
    val insertionTimestamp: Long = System.currentTimeMillis()
)
