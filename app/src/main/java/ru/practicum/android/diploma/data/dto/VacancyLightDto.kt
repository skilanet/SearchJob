package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName

data class VacancyLightDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String? = null,
    @SerializedName("employer") val employer: Employer? = Employer(),
    @SerializedName("salary") val salary: Salary? = Salary(),
    @SerializedName("area") val area: Area? = Area(),
    @SerializedName("employment") val employment: Employment? = Employment(),
    @SerializedName("schedule") val schedule: Schedule? = Schedule(),
    @SerializedName("experience") val experience: Experience? = Experience(),
)
