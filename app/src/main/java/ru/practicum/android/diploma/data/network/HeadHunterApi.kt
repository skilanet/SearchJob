package ru.practicum.android.diploma.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.data.dto.AreasResponse
import ru.practicum.android.diploma.data.dto.CountriesResponse
import ru.practicum.android.diploma.data.dto.IndustriesResponse
import ru.practicum.android.diploma.data.dto.VacanciesSearchResponse
import ru.practicum.android.diploma.data.dto.VacancyDto

interface HeadHunterApi {
    @GET("/vacancies") suspend fun searchVacancies(
        @QueryMap params: Map<String, String>,
        @HeaderMap headers: Map<String, String>
    ): Response<VacanciesSearchResponse>

    @GET("/vacancies/{vacancy_id}") suspend fun getVacancy(
        @Path("vacancy_id") id: String,
        @HeaderMap headers: Map<String, String>
    ): Response<VacancyDto>

    @GET("/industries") suspend fun getIndustries(
        @HeaderMap headers: Map<String, String>
    ): Response<IndustriesResponse>

    @GET("/areas") suspend fun getAreas(
        @HeaderMap headers: Map<String, String>
    ): Response<AreasResponse>

    @GET("/countries") suspend fun getCountries(
        @HeaderMap headers: Map<String, String>
    ): Response<CountriesResponse>

}
