package ru.practicum.android.diploma.domain.search.entity

import ru.practicum.android.diploma.domain.models.VacancyLight

sealed class Resource {
    data class Success(val data: List<VacancyLight>, val page: Int, val pages: Int, val total: Int) : Resource()
    data class Error(val code: Int) : Resource()

}
