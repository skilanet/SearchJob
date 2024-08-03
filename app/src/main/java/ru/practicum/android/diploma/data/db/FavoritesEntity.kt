package ru.practicum.android.diploma.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "favorites")
data class FavoritesEntity(
    @PrimaryKey() val id: String,
    val name: String?,
    val employerName: String?,
    val employerLogo90: String?,
    val employerLogo240: String?,
    val employerLogoOriginal: String?,
    val salaryFrom: Int?,
    val salaryTo: Int?,
    val salaryCurrency: String?,
    val area: String?,
    val date: Date?,
    val employment: String?,
    val schedule: String?,
    val experience: String?,
    val keySkills: String?,
    val description: String?,
)
