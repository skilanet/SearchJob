package ru.practicum.android.diploma.ui.filterindustry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.filterindustry.FilterIndustryInteractor
import ru.practicum.android.diploma.domain.filterindustry.entity.FilterIndustryState
import ru.practicum.android.diploma.domain.referenceinfo.entity.IndustriesResource
import ru.practicum.android.diploma.domain.referenceinfo.entity.Industry

class FilterIndustryViewModel(private val interactor: FilterIndustryInteractor) : ViewModel() {
    private val state = MutableLiveData(
        FilterIndustryState(
            "",
            false
        )
    )

    fun observeSearchState(): LiveData<FilterIndustryState> = state

    private val items = MutableLiveData<List<Industry>>(listOf())
    fun observeItems(): LiveData<List<Industry>> = items

    var filterText = ""

    fun onFilterTextChanged(
        p0: CharSequence?,
        p1: Int,
        p2: Int,
        p3: Int
    ) {

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
                    items.postValue(list)
                }
            }
        }
    }

    fun onChecked(industry: Industry) {

    }

}



