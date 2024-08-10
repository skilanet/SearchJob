package ru.practicum.android.diploma.domain.models

data class Filter(
    val area: String?,
    val industry: String?,
    val onlyWithSalary: Boolean?,
    val salary: Int?
)
