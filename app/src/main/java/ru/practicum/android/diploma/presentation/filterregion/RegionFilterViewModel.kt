package ru.practicum.android.diploma.presentation.filterregion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.filter.FilterCacheInteractor
import ru.practicum.android.diploma.domain.filter.FilterInteractor
import ru.practicum.android.diploma.domain.filter.entity.AreaEntity
import ru.practicum.android.diploma.domain.filter.entity.FilterSetting
import ru.practicum.android.diploma.presentation.filterregion.state.RegionFilterState
import ru.practicum.android.diploma.util.SingleEventLiveData

class RegionFilterViewModel(
    private val filterInteractor: FilterInteractor,
    private val filterCacheInteractor: FilterCacheInteractor
) : ViewModel() {
    private var screenStateLiveData = MutableLiveData<RegionFilterState>()
    val searchTextLiveData = MutableLiveData("")
    private val regionAddedEvent = SingleEventLiveData<Boolean>()
    private var latestSearchText: String? = null

    fun getScreenStateLiveData(): LiveData<RegionFilterState> = screenStateLiveData
    fun observeSearchTextLiveData(): LiveData<String> = searchTextLiveData
    fun observeAddRegionAddedEvent(): LiveData<Boolean> = regionAddedEvent

    init {
        getRegionList()
    }

    private fun getRegionList() {
        viewModelScope.launch {
            val savedFilter = filterCacheInteractor.getCache()
            filterInteractor.getRegionsList(savedFilter?.area?.country?.id).collect { resource ->
                if (resource.error || resource.data.isEmpty()) {
                    screenStateLiveData.postValue(RegionFilterState.Error)
                } else {
                    screenStateLiveData.postValue(RegionFilterState.Content(resource.data))
                }
            }
        }
    }

    fun addRegionToFilter(area: AreaEntity) {
        viewModelScope.launch {
            filterCacheInteractor.writeCache(FilterSetting.Area(area.parentCountry, area))
            regionAddedEvent.postValue(true)
        }
    }

    fun onClearText() {
        clear()
    }

    fun onSearchTextChanged(
        p0: CharSequence?,
        p1: Int,
        p2: Int,
        p3: Int
    ) {
        val text = p0.toString()
        if (text != latestSearchText && text.isNotEmpty()) {
            latestSearchText = text
            screenStateLiveData.postValue(RegionFilterState.Filter(text))
        }
    }

    fun onEditorActionDone() {
        val text = searchTextLiveData.value.toString()
        if (text != latestSearchText && text.isNotEmpty()) {
            screenStateLiveData.postValue(RegionFilterState.Filter(text))
        }
    }

    private fun clear() {
        latestSearchText = ""
        searchTextLiveData.postValue("")
    }
}
