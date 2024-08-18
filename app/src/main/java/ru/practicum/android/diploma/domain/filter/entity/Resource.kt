package ru.practicum.android.diploma.domain.filter.entity

sealed interface Resource {
    data class Success(val data: List<AreaEntity>) : Resource
    data class Error(val code: Int) : Resource
}
