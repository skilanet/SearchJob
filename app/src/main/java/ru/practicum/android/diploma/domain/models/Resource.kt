package ru.practicum.android.diploma.domain.models

sealed class Resource {
    data class Error(val code: Int) : Resource()

}
