package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.data.dto.GetVacancyResponse
import ru.practicum.android.diploma.data.dto.VacanciesSearchResponse

interface HeadHunterApi {
    @GET("/vacancies")
    suspend fun searchVacancies(
        @QueryMap params: Map<String, String>,
        @HeaderMap headers: Map<String, String>
    ): VacanciesSearchResponse

    @GET("/vacancies/{vacancy_id}")
    suspend fun getVacancy(
        @Path("vacancy_id") id: String,
        @HeaderMap headers: Map<String, String>
    ): GetVacancyResponse
}
