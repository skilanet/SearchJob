package ru.practicum.android.diploma.data.network.impl

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.dto.AreasRequest
import ru.practicum.android.diploma.data.dto.CountriesRequest
import ru.practicum.android.diploma.data.dto.FilterDto
import ru.practicum.android.diploma.data.dto.GetVacancyRequest
import ru.practicum.android.diploma.data.dto.GetVacancyResponse
import ru.practicum.android.diploma.data.dto.IndustriesRequest
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.VacanciesSearchRequest
import ru.practicum.android.diploma.data.network.HeadHunterApi
import ru.practicum.android.diploma.data.network.NetworkClient

class RetrofitNetworkClient(
    private val headHunterService: HeadHunterApi,
    private val connectivityManager: ConnectivityManager
) : NetworkClient {
    override suspend fun doRequest(request: Any): Response {
        if (!isConnected()) {
            val response = Response(resultCode = NO_CONNECTION)
            return response
        }
        val response = when (request) {
            is VacanciesSearchRequest -> getVacanciesSearchResponse(request)
            is GetVacancyRequest -> getVacanciesResponse(request)
            is IndustriesRequest -> getIndustriesResponse()
            is AreasRequest -> getAreasResponse()
            is CountriesRequest -> getCountriesResponse()
            else -> {
                Response(BAD_REQUEST)
            }
        }
        return response
    }

    private suspend fun getIndustriesResponse(): Response {
        val headers = getCommonHeaders()
        val res = headHunterService.getIndustries(
            headers = headers
        )
        val response = res.body() ?: Response()
        response.resultCode = res.code()

        return response

    }

    private suspend fun getAreasResponse(): Response {
        val headers = getCommonHeaders()
        val res = headHunterService.getAreas(
            headers = headers
        )
        val response = res.body() ?: Response()
        response.resultCode = res.code()

        return response

    }

    private suspend fun getCountriesResponse(): Response {
        val headers = getCommonHeaders()
        val res = headHunterService.getCountries(
            headers = headers
        )
        val response = res.body() ?: Response()
        response.resultCode = res.code()

        return response

    }

    private suspend fun getVacanciesResponse(request: GetVacancyRequest): Response {
        val headers = getCommonHeaders()
        val res = headHunterService.getVacancy(
            id = request.id,
            headers = headers
        )
        val body = res.body()
        val response = if (body != null) {
            GetVacancyResponse(data = body)
        } else {
            Response()
        }
        response.resultCode = res.code()

        return response

    }

    private suspend fun getVacanciesSearchResponse(request: VacanciesSearchRequest): Response {
        val headers = getCommonHeaders()
        val params = getSearchParams(request)
        val res = headHunterService.searchVacancies(
            params = params,
            headers = headers
        )
        val response = res.body() ?: Response()
        response.resultCode = res.code()

        return response

    }

    private fun getSearchParams(
        request: VacanciesSearchRequest
    ): Map<String, String> {
        val params = getParamsFromFilterDto(request.filterDto)
        params["page"] = request.page
        params["per_page"] = request.perPage
        params["text"] = request.text
        return params.mapValues { it.value.toString() }
    }

    private fun getParamsFromFilterDto(filterDto: FilterDto?): MutableMap<String, Any> {
        val params: MutableMap<String, Any> = mutableMapOf()
        if (filterDto != null) {
            if (!filterDto.area.isNullOrEmpty()) {
                params["area"] = filterDto.area
            }

            if (filterDto.onlyWithSalary != null) {
                params["only_with_salary"] = filterDto.onlyWithSalary
            }

            if (filterDto.salary != null) {
                params["salary"] = filterDto.salary
            }

            if (!filterDto.industry.isNullOrEmpty()) {
                params["industry"] = filterDto.industry
            }

        }
        return params
    }

    private fun isConnected(): Boolean {
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities?.run {
            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || hasTransport(
                NetworkCapabilities.TRANSPORT_ETHERNET
            )
        } ?: false
    }

    private fun getCommonHeaders(): Map<String, String> {
        return mapOf(
            "HH-User-Agent" to "Application Name (${APP_NAME})",
            "Authorization" to "Bearer ${BuildConfig.HH_ACCESS_TOKEN}"
        )

    }

    companion object {
        const val SUCCESS = 200
        const val BAD_REQUEST = 400
        const val NOT_FOUND = 404
        const val NO_CONNECTION = -1
        const val APP_NAME = "YP_Diploma"
    }
}
