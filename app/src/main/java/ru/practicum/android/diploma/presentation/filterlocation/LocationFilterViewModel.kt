package ru.practicum.android.diploma.presentation.filterlocation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.filter.FilterInteractor
import ru.practicum.android.diploma.domain.filter.entity.FilterSetting
import ru.practicum.android.diploma.presentation.filterlocation.state.LocationState

class LocationFilterViewModel(
    private val filterInteractor: FilterInteractor
) : ViewModel() {
    private var screenStateLiveData = MutableLiveData<LocationState>()

    fun observeScreenStateLiveData(): LiveData<LocationState> = screenStateLiveData

    init {
        getSavedLocationFilterSettings()
    }

    fun getSavedLocationFilterSettings() {
        viewModelScope.launch {
            val savedFilter = filterInteractor.getFilter()
            screenStateLiveData.postValue(LocationState(savedFilter?.area?.country, savedFilter?.area?.region))
        }
    }

    fun clearCountry() {
        viewModelScope.launch {
            filterInteractor.saveSetting(FilterSetting.Area(null, null))
            screenStateLiveData.postValue(LocationState(null, null))
        }
    }

    fun clearRegion() {
        viewModelScope.launch {
            val savedFilter = filterInteractor.getFilter()
            filterInteractor.saveSetting(FilterSetting.Area(savedFilter?.area?.country, null))
            screenStateLiveData.postValue(LocationState(savedFilter?.area?.country, null))
        }
    }
}
