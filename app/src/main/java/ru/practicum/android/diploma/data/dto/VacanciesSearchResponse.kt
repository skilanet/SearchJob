package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName

data class VacanciesSearchResponse(
    val items: List<VacancyLightDto>,
    val page: Int,
    val found: Int,
    val pages: Int,
    @SerializedName("per_page") val perPage: Int
) : Response()
