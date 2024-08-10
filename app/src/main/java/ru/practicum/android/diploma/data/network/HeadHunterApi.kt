package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.data.dto.VacanciesSearchResponse

interface HeadHunterApi {
    @GET("/vacancies")
    suspend fun searchVacancies(
        @QueryMap params: Map<String, Any>,
        @HeaderMap headers: Map<String, String>
    ): VacanciesSearchResponse
}
