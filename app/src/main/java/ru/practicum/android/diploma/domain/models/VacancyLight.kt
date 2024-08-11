package ru.practicum.android.diploma.domain.models

data class VacancyLight(
    val id: String,
    val name: String = "",
    val employerName: String = "",
    val employerLogo90: String?,
    val employerLogo240: String?,
    val employerLogoOriginal: String?,
    val resultSalary: String
)
