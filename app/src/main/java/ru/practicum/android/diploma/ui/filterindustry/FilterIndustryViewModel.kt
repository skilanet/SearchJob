package ru.practicum.android.diploma.ui.filterindustry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.filter.FilterInteractor
import ru.practicum.android.diploma.domain.filter.entity.FilterSetting
import ru.practicum.android.diploma.domain.filterindustry.FilterIndustryInteractor
import ru.practicum.android.diploma.domain.filterindustry.entity.FilterIndustryListState
import ru.practicum.android.diploma.domain.filterindustry.entity.FilterIndustryState
import ru.practicum.android.diploma.domain.referenceinfo.entity.IndustriesResource
import ru.practicum.android.diploma.domain.referenceinfo.entity.Industry

class FilterIndustryViewModel(
    private val interactor: FilterIndustryInteractor,
    private val filterInteractor: FilterInteractor
) : ViewModel() {
    private var selected: Industry? = null

    init {
        val filter = filterInteractor.getFilter()
        if (!filter?.industry?.id.isNullOrEmpty()) {
            selected = Industry(
                filter?.industry?.id ?: "",
                filter?.industry?.name ?: ""
            )
        }

        load()
    }

    private val state = MutableLiveData(
        FilterIndustryState(
            selected?.name ?: "",
            selected != null
        )
    )

    fun observeIndustryState(): LiveData<FilterIndustryState> = state

    val filterText = MutableLiveData(selected?.name ?: "")
    private val items = MutableLiveData(
        FilterIndustryListState(
            listOf(),
            null
        )
    )

    fun observeItems(): LiveData<FilterIndustryListState> = items

    fun onFilterTextChanged(
        p0: CharSequence?,
        p1: Int,
        p2: Int,
        p3: Int
    ) {
        state.value = state.value?.copy(filterText = p0.toString())
    }

    private fun load() {
        viewModelScope.launch {
            interactor.getIndustries().collect {
                if (it is IndustriesResource.Success) {
                    val list: MutableList<Industry> = mutableListOf()
                    for (item in it.data) {
                        list.add(
                            Industry(
                                item.id,
                                item.name
                            )
                        )

                        if (item.industries != null) {
                            for (industry in item.industries) {
                                list.add(
                                    Industry(
                                        industry.id,
                                        industry.name
                                    )
                                )
                            }

                        }
                    }
                    items.postValue(
                        FilterIndustryListState(
                            list,
                            selected
                        )
                    )
                }
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
        filterInteractor.saveSetting(setting)
        filterText.value = selected?.name ?: ""
    }

}
