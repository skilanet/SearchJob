package ru.practicum.android.diploma.data.dto

data class VacanciesSearchRequest(
    val filterDto: FilterDto?,
    val text: String,
    val page: Int,
    val perPage: Int
)
