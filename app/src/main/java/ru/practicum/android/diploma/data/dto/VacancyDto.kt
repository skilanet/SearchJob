package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.data.dto.vacancySubClasses.Area
import ru.practicum.android.diploma.data.dto.vacancySubClasses.Employer
import ru.practicum.android.diploma.data.dto.vacancySubClasses.Employment
import ru.practicum.android.diploma.data.dto.vacancySubClasses.Experience
import ru.practicum.android.diploma.data.dto.vacancySubClasses.KeySkills
import ru.practicum.android.diploma.data.dto.vacancySubClasses.Salary
import ru.practicum.android.diploma.data.dto.vacancySubClasses.Schedule

data class VacancyDto(
    @SerializedName("id") var id: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("employer") var employer: Employer? = Employer(),
    @SerializedName("salary") var salary: Salary? = Salary(),
    @SerializedName("area") var area: Area? = Area(),
    @SerializedName("employment") var employment: Employment? = Employment(),
    @SerializedName("schedule") var schedule: Schedule? = Schedule(),
    @SerializedName("experience") var experience: Experience? = Experience(),
    @SerializedName("key_skills") var keySkills: ArrayList<KeySkills> = arrayListOf(),
    @SerializedName("description") var description: String? = null,
)
