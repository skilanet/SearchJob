package ru.practicum.android.diploma.data.filter.impl

import android.content.SharedPreferences
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.data.dto.CountriesRequest
import ru.practicum.android.diploma.data.dto.CountriesResponse
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.filter.FilterRepository
import ru.practicum.android.diploma.domain.filter.entity.Filter
import ru.practicum.android.diploma.domain.filter.entity.FilterSetting
import ru.practicum.android.diploma.domain.filter.entity.Resource
import ru.practicum.android.diploma.domain.models.ErrorCode
import ru.practicum.android.diploma.util.mappers.FilterMapper

class FilterRepositoryImpl(
    private val sharedPreference: SharedPreferences,
    private val gson: Gson,
    private val networkClient: NetworkClient,
    private val filterMapper: FilterMapper
) : FilterRepository {
    override fun saveSetting(setting: FilterSetting) {
        val filter = getFromStorage() ?: Filter()

        updateSetting(
            filter,
            setting
        )
        saveToStorage(filter)

    }

    override fun getFilter(): Filter? {
        return getFromStorage()
    }

    override fun isFilterPresent(): Boolean {
        val filter = getFromStorage() ?: return false
        val salaryFilled = (filter.salary?.salary ?: 0) != 0
        val withSalaryFilled = filter.salary?.onlyWithSalary == true
        val regionFilled = !filter.area?.region?.id.isNullOrEmpty()
        val industryFilled = !filter.industry?.id.isNullOrEmpty()
        val countryFilled = !filter.area?.country?.id.isNullOrEmpty()
        return salaryFilled or withSalaryFilled or regionFilled or industryFilled or countryFilled
    }

    override fun getCountries(): Flow<Resource> = flow {
        val response = networkClient.doRequest(CountriesRequest)
        val resource = if (response !is CountriesResponse) {
            Resource.Error(ErrorCode.BAD_REQUEST)
        } else {
            when (response.resultCode) {
                ErrorCode.SUCCESS -> Resource.Success(response.data.map {
                    filterMapper.fromDtoToArea(it)
                })
                ErrorCode.NOT_FOUND -> Resource.Error(ErrorCode.NOT_FOUND)
                ErrorCode.NO_CONNECTION -> Resource.Error(ErrorCode.NO_CONNECTION)
                else -> Resource.Error(ErrorCode.BAD_REQUEST)
            }
        }
        emit(resource)
    }.flowOn(Dispatchers.IO).catch { Resource.Error(ErrorCode.BAD_REQUEST) }

    private fun updateSetting(
        filter: Filter,
        setting: FilterSetting
    ) {
        when (setting) {
            is FilterSetting.Salary -> {
                filter.salary = setting
            }

            is FilterSetting.Industry -> {
                filter.industry = setting
            }

            is FilterSetting.Area -> {
                filter.area = setting
            }
        }

    }

    private fun saveToStorage(filter: Filter) {
        sharedPreference.edit().putString(
            FILTER_KEY,
            gson.toJson(filter)
        ).apply()
    }

    private fun getFromStorage(): Filter? {
        val filterAsJson = sharedPreference.getString(
            FILTER_KEY,
            null
        )

        return if (filterAsJson != null) {
            gson.fromJson(
                filterAsJson,
                Filter::class.java
            )
        } else {
            null
        }

    }

    companion object {
        const val FILTER_KEY = "FILTER"

    }
}
