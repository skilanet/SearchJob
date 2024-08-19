package ru.practicum.android.diploma.presentation.filterregion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.filter.FilterInteractor
import ru.practicum.android.diploma.domain.filter.entity.AreaEntity
import ru.practicum.android.diploma.domain.filter.entity.FilterSetting
import ru.practicum.android.diploma.presentation.filterregion.state.RegionFilterState
import ru.practicum.android.diploma.util.SingleEventLiveData
import ru.practicum.android.diploma.util.debounce

class RegionFilterViewModel(private val filterInteractor: FilterInteractor) : ViewModel() {
    private var screenStateLiveData = MutableLiveData<RegionFilterState>()
    private val searchTextLiveData = MutableLiveData<String>("")
    private val regionAddedEvent = SingleEventLiveData<Boolean>()
    private var latestSearchText: String? = null

    fun getScreenStateLiveData(): LiveData<RegionFilterState> = screenStateLiveData
    fun getSearchTextLiveData(): LiveData<String> = searchTextLiveData
    fun observeAddRegionAddedEvent(): LiveData<Boolean> = regionAddedEvent

    init {
        getRegionList()
    }

    private fun getRegionList() {
        viewModelScope.launch {
            val savedFilter = filterInteractor.getFilter()
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
            filterInteractor.saveSetting(FilterSetting.Area(area.parentCountry, area))
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
        onTextChangedDebounce(p0.toString())
    }

    fun onEditorActionDone() {
        val text = searchTextLiveData.value.toString()
        if (text != latestSearchText && text.isNotEmpty()) {
            screenStateLiveData.postValue(RegionFilterState.Filter(text))
        }
    }

    private val onTextChangedDebounce = debounce<String>(
        SEARCH_DEBOUNCE_DELAY,
        viewModelScope,
        true
    ) { text ->
        if (text != latestSearchText && text.isNotEmpty()) {
            latestSearchText = text
            screenStateLiveData.postValue(RegionFilterState.Filter(text))
        }
    }

    private fun clear() {
        latestSearchText = ""
        searchTextLiveData.postValue("")
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
