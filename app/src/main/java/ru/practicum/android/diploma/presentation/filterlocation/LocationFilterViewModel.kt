package ru.practicum.android.diploma.presentation.filterlocation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.filter.FilterCacheInteractor
import ru.practicum.android.diploma.domain.filter.FilterInteractor
import ru.practicum.android.diploma.domain.filter.entity.Filter
import ru.practicum.android.diploma.domain.filter.entity.FilterSetting
import ru.practicum.android.diploma.presentation.filterlocation.state.LocationState
import ru.practicum.android.diploma.util.SingleEventLiveData

class LocationFilterViewModel(
    private val filterInteractor: FilterInteractor,
    private val filterCacheInteractor: FilterCacheInteractor
) : ViewModel() {
    private var screenStateLiveData = MutableLiveData<LocationState>()
    private val cachedRegionInvalidatedEvent = SingleEventLiveData<Boolean>()
    private var savedFilterSetting: Filter? = null

    fun observeScreenStateLiveData(): LiveData<LocationState> = screenStateLiveData
    fun observeCachedRegionInvalidatedEvent(): LiveData<Boolean> = cachedRegionInvalidatedEvent

    init {
        getSavedLocationFilterSettings()
    }

    fun getSavedLocationFilterSettings() {
        viewModelScope.launch {
            savedFilterSetting = filterCacheInteractor.getCache()
            screenStateLiveData.postValue(
                LocationState(
                    savedFilterSetting?.area?.country,
                    savedFilterSetting?.area?.region
                )
            )
        }
    }

    fun invalidateChangedLocationFilter() {
        viewModelScope.launch {
            filterCacheInteractor.writeCache(
                FilterSetting.Area(
                    savedFilterSetting?.area?.country,
                    savedFilterSetting?.area?.region
                )
            )
            cachedRegionInvalidatedEvent.postValue(true)
        }
    }

    fun clearCountry() {
        viewModelScope.launch {
            filterCacheInteractor.writeCache(FilterSetting.Area(null, null))
            screenStateLiveData.postValue(LocationState(null, null))
        }
    }

    fun clearRegion() {
        viewModelScope.launch {
            val savedFilter = filterInteractor.getFilter()
            filterCacheInteractor.writeCache(FilterSetting.Area(savedFilter?.area?.country, null))
            screenStateLiveData.postValue(LocationState(savedFilter?.area?.country, null))
        }
    }
}
