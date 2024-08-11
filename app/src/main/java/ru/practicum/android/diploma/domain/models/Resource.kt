package ru.practicum.android.diploma.domain.models

sealed class Resource {
    data class Success<T>(val data: T) : Resource()
    data class Error(val code: Int) : Resource()

}
