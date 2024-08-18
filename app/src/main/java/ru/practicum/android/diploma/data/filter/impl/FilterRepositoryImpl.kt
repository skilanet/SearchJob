package ru.practicum.android.diploma.data.filter.impl

import android.content.SharedPreferences
import com.google.gson.Gson
import ru.practicum.android.diploma.domain.filter.FilterRepository
import ru.practicum.android.diploma.domain.filter.entity.Filter
import ru.practicum.android.diploma.domain.filter.entity.FilterSetting

class FilterRepositoryImpl(
    private val sharedPreference: SharedPreferences,
    private val gson: Gson
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
        val salaryFilled = filter.salary?.salary ?: 0 != 0
        val withSalaryFilled = filter.salary?.onlyWithSalary == true
        val regionFilled = !filter.area?.region?.id.isNullOrEmpty()
        val industryFilled = !filter.industry?.id.isNullOrEmpty()
        val countryFilled = !filter.area?.country?.id.isNullOrEmpty()
        return salaryFilled or withSalaryFilled or regionFilled or industryFilled or countryFilled
    }

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
