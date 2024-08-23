package ru.practicum.android.diploma.ui.filterindustry

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.filter.FilterCacheInteractor
import ru.practicum.android.diploma.domain.filter.entity.Filter
import ru.practicum.android.diploma.domain.filter.entity.FilterSetting
import ru.practicum.android.diploma.domain.filterindustry.FilterIndustryInteractor
import ru.practicum.android.diploma.domain.filterindustry.entity.FilterIndustryListState
import ru.practicum.android.diploma.domain.filterindustry.entity.FilterIndustryState
import ru.practicum.android.diploma.domain.referenceinfo.entity.IndustriesResource
import ru.practicum.android.diploma.domain.referenceinfo.entity.Industry
import ru.practicum.android.diploma.util.SingleEventLiveData

class FilterIndustryViewModel(
    private val interactor: FilterIndustryInteractor,
    private val filterCacheInteractor: FilterCacheInteractor
) : ViewModel() {
    private var selected: Industry? = null
    private var savedFilterSetting: Filter? = null

    init {
        savedFilterSetting = filterCacheInteractor.getCache()
        if (!savedFilterSetting?.industry?.id.isNullOrEmpty()) {
            selected = Industry(
                savedFilterSetting?.industry?.id ?: "", savedFilterSetting?.industry?.name ?: ""
            )
        }

        load()
    }

    private val state = MutableLiveData(
        FilterIndustryState(
            "",
            selected != null
        )
    )

    private val changesInvalidatedEvent = SingleEventLiveData<Boolean>()

    fun observeIndustryState(): LiveData<FilterIndustryState> = state
    fun observeChangesInvalidatedEvent(): LiveData<Boolean> = changesInvalidatedEvent

    val filterText = MutableLiveData("")
    private val items = MutableLiveData<FilterIndustryListState>()

    fun observeItems(): LiveData<FilterIndustryListState> = items

    fun onFilterTextChanged(
        p0: CharSequence?,
        p1: Int,
        p2: Int,
        p3: Int
    ) {
        state.value = state.value?.copy(filterText = p0.toString())
        Log.d("_TAG", "filterText: ${filterText.value} state: ${state.value} items: ${items.value}")
    }

    fun onClearText() {
        state.postValue(state.value?.copy(filterText = ""))
        filterText.postValue("")
    }

    private fun load() {
        viewModelScope.launch {
            interactor.getIndustries()
                .collect {
                    items.postValue(
                        when (it) {
                            is IndustriesResource.Success -> {
                                if (it.data.isEmpty()) {
                                    FilterIndustryListState.Error
                                } else {
                                    FilterIndustryListState.Content(it.data, selected)
                                }
                            }
                            is IndustriesResource.Error -> {
                                FilterIndustryListState.Error
                            }
                        }
                    )
                }
        }
    }

    fun onChecked(industry: Industry?) {
        selected = industry
        state.value = state.value?.copy(isSaveEnable = industry != null)
        val setting = if (industry != null) {
            FilterSetting.Industry(
                industry.id,
                industry.name
            )
        } else {
            FilterSetting.Industry()
        }
        filterCacheInteractor.writeCache(setting) // filterText.value = selected?.name ?: ""
    }

    fun invalidateFilterChanges() {
        viewModelScope.launch {
            filterCacheInteractor.writeCache(
                FilterSetting.Industry(savedFilterSetting?.industry?.id, savedFilterSetting?.industry?.name)
            )
            changesInvalidatedEvent.postValue(true)
        }
    }

}
