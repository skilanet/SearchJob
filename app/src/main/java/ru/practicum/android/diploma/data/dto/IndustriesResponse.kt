package ru.practicum.android.diploma.data.dto

data class IndustriesResponse(
    val id: String,
    val name: String,
    val industries: List<Industry>
) : Response()
