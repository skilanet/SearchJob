package ru.practicum.android.diploma.data.dto

data class VacanciesSearchRequest(
    val filterDto: FilterDto?,
    val page: Int,
    val perPage: Int
)
