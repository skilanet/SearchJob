package ru.practicum.android.diploma.domain.referenceinfo.entity

sealed class IndustriesResource {
    data class Success(val data: List<Industry>) : IndustriesResource()
    data class Error(val code: Int) : IndustriesResource()

}
