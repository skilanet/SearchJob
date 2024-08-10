package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName

class VacanciesSearchResponse(
    val items: List<VacancyDto>,
    val page: Int,
    val found: Int,
    val pages: Int,
    @SerializedName("per_page") val perPage: Int
) : Response()
