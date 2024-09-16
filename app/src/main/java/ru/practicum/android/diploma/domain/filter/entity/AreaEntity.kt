package ru.practicum.android.diploma.domain.filter.entity

data class AreaEntity(
    val id: String,
    val name: String,
    val parentCountry: AreaEntity? = null
)
