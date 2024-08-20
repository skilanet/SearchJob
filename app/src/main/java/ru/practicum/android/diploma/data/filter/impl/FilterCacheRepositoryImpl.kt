package ru.practicum.android.diploma.data.filter.impl

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import ru.practicum.android.diploma.domain.filter.FilterCacheRepository
import ru.practicum.android.diploma.domain.filter.entity.Filter
import ru.practicum.android.diploma.domain.filter.entity.FilterSetting

class FilterCacheRepositoryImpl(
    private val sharedPreference: SharedPreferences,
    private val gson: Gson,
) : FilterCacheRepository {
    override fun createCache() {
        val filter = getFromStorage() ?: Filter()
        sharedPreference.edit()
            .putString(
                FILTER_CACHE_KEY,
                gson.toJson(filter)
            ).apply()
    }

    override fun getCache(): Filter? {
        val filterAsJson = sharedPreference.getString(
            FILTER_CACHE_KEY,
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

    override fun commitCache() {
        val cachedFilterJson = sharedPreference.getString(
            FILTER_CACHE_KEY,
            null
        )
        if (cachedFilterJson != null) {
            sharedPreference.edit().putString(
                FILTER_KEY,
                cachedFilterJson
            )
                .putString(
                    FILTER_CACHE_KEY,
                    null
                )
                .apply()
        }
    }

    override fun isCachedFilterChanged(): Boolean {
        val cachedFilterJson = sharedPreference.getString(
            FILTER_CACHE_KEY,
            null
        )
        Log.i("cached", cachedFilterJson.toString())
        val filterJson = sharedPreference.getString(
            FILTER_KEY,
            null
        )
        Log.i("cached", filterJson.toString())
        if (filterJson != null) {
            val cachedFilter = gson.fromJson(
                cachedFilterJson,
                Filter::class.java
            )
            val savedFilter = gson.fromJson(
                filterJson,
                Filter::class.java
            )
            Log.i("compare", (cachedFilter != savedFilter).toString())
            return cachedFilter != savedFilter
        } else {
            return false
        }

    }

    override fun isCachedFilterEmpty(): Boolean {
        val cachedFilterJson = sharedPreference.getString(
            FILTER_CACHE_KEY,
            null
        )
        if (cachedFilterJson != null) {
            val cachedFilter = gson.fromJson(
                cachedFilterJson,
                Filter::class.java
            )
            val salaryFilled = cachedFilter.salary?.salary ?: 0 != 0
            val withSalaryFilled = cachedFilter.salary?.onlyWithSalary == true
            val regionFilled = !cachedFilter.area?.region?.id.isNullOrEmpty()
            val industryFilled = !cachedFilter.industry?.id.isNullOrEmpty()
            val countryFilled = !cachedFilter.area?.country?.id.isNullOrEmpty()
            return !salaryFilled and !withSalaryFilled and !regionFilled and !industryFilled and !countryFilled
        } else {
            return true
        }
    }

    override fun writeCache(setting: FilterSetting) {
        val filter = getCache() ?: Filter()

        updateSetting(
            filter,
            setting
        )
        sharedPreference.edit()
            .putString(
                FILTER_CACHE_KEY,
                gson.toJson(filter)
            )
            .apply()
    }

    override fun invalidateCache() {
        sharedPreference.edit()
            .putString(
                FILTER_CACHE_KEY,
                null
            )
            .apply()
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

    companion object {
        const val FILTER_CACHE_KEY = "FILTER_CACHE"
        const val FILTER_KEY = "FILTER"
    }
}
