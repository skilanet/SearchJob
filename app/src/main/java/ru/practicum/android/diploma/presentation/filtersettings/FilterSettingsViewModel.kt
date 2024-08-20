package ru.practicum.android.diploma.presentation.filtersettings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.filter.FilterCacheInteractor
import ru.practicum.android.diploma.domain.filter.FilterInteractor
import ru.practicum.android.diploma.domain.filter.entity.Filter
import ru.practicum.android.diploma.domain.filter.entity.FilterSetting
import ru.practicum.android.diploma.presentation.filtersettings.state.FilterSettingsState
import ru.practicum.android.diploma.util.SingleEventLiveData

class FilterSettingsViewModel(
    private val filterInteractor: FilterInteractor,
    private val filterCacheInteractor: FilterCacheInteractor
) : ViewModel() {
    private var screenStateLiveData = MutableLiveData<FilterSettingsState>()
    private var applyButtonLiveData = MutableLiveData<Boolean>()
    private var resetButtonLiveData = MutableLiveData<Boolean>()
    private var filtersAppliedEvent = SingleEventLiveData<Boolean>()
    private var currentSalary: Int? = null

    fun observeScreenStateLiveData(): LiveData<FilterSettingsState> = screenStateLiveData
    fun observeApplyButtonLiveData(): LiveData<Boolean> = applyButtonLiveData
    fun observeResetButtonLiveData(): LiveData<Boolean> = resetButtonLiveData
    fun observeFiltersAddedEvent(): LiveData<Boolean> = filtersAppliedEvent

    init {
        viewModelScope.launch {
            val filterSettings = if (filterInteractor.getFilter() == null) {
                filterInteractor.initializeEmptyFilter()
                filterInteractor.getFilter()
            } else {
                filterInteractor.getFilter()
            }
            filterCacheInteractor.createCache()
            screenStateLiveData.postValue(FilterSettingsState(filterSettings ?: Filter()))
            applyButtonLiveData.postValue(false)
            resetButtonLiveData.postValue(filterCacheInteractor.isCachedFilterEmpty())
        }
    }

    fun onSalaryTextChanged(
        p0: CharSequence?,
        p1: Int,
        p2: Int,
        p3: Int
    ) {
        Log.i("salary", p0.toString())
        currentSalary = p0?.toString()?.toIntOrNull()
        if (currentSalary ?: -1 >= 0) {
            viewModelScope.launch {
                filterCacheInteractor.writeCache(FilterSetting.Salary(currentSalary))
            }
        }
        applyButtonLiveData.postValue(filterCacheInteractor.isCachedFilterChanged())
    }

    fun onActionDone() {
        viewModelScope.launch {
            filterCacheInteractor.writeCache(FilterSetting.Salary(currentSalary))
            Log.i("changed", filterCacheInteractor.isCachedFilterChanged().toString())
            applyButtonLiveData.postValue(filterCacheInteractor.isCachedFilterChanged())
            resetButtonLiveData.postValue(filterCacheInteractor.isCachedFilterEmpty())
        }
    }

    fun updateFilterData() {
        viewModelScope.launch {
            val cachedSettings = filterCacheInteractor.getCache()
            screenStateLiveData.postValue(FilterSettingsState(cachedSettings ?: Filter()))
            applyButtonLiveData.postValue(filterCacheInteractor.isCachedFilterChanged())
            resetButtonLiveData.postValue(filterCacheInteractor.isCachedFilterEmpty())
        }
    }

    fun resetFilters() {
        viewModelScope.launch {
            filterInteractor.deleteFilters()
            filterCacheInteractor.invalidateCache()
            filterInteractor.initializeEmptyFilter()
            val filterSettings = filterInteractor.getFilter()
            filterCacheInteractor.createCache()
            screenStateLiveData.postValue(FilterSettingsState(filterSettings ?: Filter()))
            applyButtonLiveData.postValue(false)
            resetButtonLiveData.postValue(filterCacheInteractor.isCachedFilterEmpty())
        }
    }

    fun addSalaryCheckFilter(isChecked: Boolean) {
        viewModelScope.launch {
            filterCacheInteractor.writeCache(FilterSetting.Salary(salary = currentSalary, onlyWithSalary = isChecked))
            applyButtonLiveData.postValue(filterCacheInteractor.isCachedFilterChanged())
            resetButtonLiveData.postValue(filterCacheInteractor.isCachedFilterEmpty())
        }
    }

    fun invalidateCache() {
        viewModelScope.launch {
            filterCacheInteractor.invalidateCache()
        }
    }

    fun applyFilters() {
        viewModelScope.launch {
            filterCacheInteractor.commitCache()
            filtersAppliedEvent.postValue(true)
        }
    }

}
