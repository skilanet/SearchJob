package ru.practicum.android.diploma.data.dto

data class FilterDto(
    val area: String?,
    val industry: String?,
    val onlyWithSalary: Boolean?,
    val salary: Int?
)
