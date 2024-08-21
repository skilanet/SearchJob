package ru.practicum.android.diploma.presentation.filtercountry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.filter.FilterCacheInteractor
import ru.practicum.android.diploma.domain.filter.FilterInteractor
import ru.practicum.android.diploma.domain.filter.entity.AreaEntity
import ru.practicum.android.diploma.domain.filter.entity.FilterSetting
import ru.practicum.android.diploma.domain.filter.entity.Resource
import ru.practicum.android.diploma.presentation.filtercountry.state.CountryFilterState
import ru.practicum.android.diploma.util.SingleEventLiveData

class CountryFilterViewModel(
    private val filterInteractor: FilterInteractor,
    private val filterCacheInteractor: FilterCacheInteractor
) : ViewModel() {
    private var screenStateLiveData = MutableLiveData<CountryFilterState>()
    private val countryAddedEvent = SingleEventLiveData<Boolean>()

    fun observeScreenStateLiveData(): LiveData<CountryFilterState> = screenStateLiveData
    fun observeCountryAddedEvent(): LiveData<Boolean> = countryAddedEvent

    init {
        getCountryList()
    }

    private fun getCountryList() {
        viewModelScope.launch {
            filterInteractor.getCountries().collect { resource ->
                when (resource) {
                    is Resource.Error -> Unit
                    is Resource.Success -> {
                        screenStateLiveData.postValue(CountryFilterState(resource.data))
                    }
                }
            }
        }
    }

    fun addCountryToFilter(area: AreaEntity) {
        viewModelScope.launch {
            filterCacheInteractor.writeCache(FilterSetting.Area(area, null))
            countryAddedEvent.postValue(true)
        }
    }
}
