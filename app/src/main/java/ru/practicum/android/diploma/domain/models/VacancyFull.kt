package ru.practicum.android.diploma.domain.models

data class VacancyFull(
    val id: String,
    val name: String = "",
    val employerName: String = "",
    val employerLogo90: String?,
    val employerLogo240: String?,
    val employerLogoOriginal: String?,
    val salaryFrom: Int? = null,
    val salaryTo: Int? = null,
    val salaryCurrency: String? = "",
    val area: String?,
    val employment: String = "",
    val schedule: String = "",
    val experience: String = "",
    val keySkills: List<String> = arrayListOf(),
    val description: String = "",
    val url: String? = null,
    val alternativeUrl: String? = null
)
