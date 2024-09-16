package ru.practicum.android.diploma.presentation.filtercountry.state

import ru.practicum.android.diploma.domain.filter.entity.AreaEntity

sealed interface CountryFilterState {
    data class Content(val countries: List<AreaEntity>) : CountryFilterState
    data object Error : CountryFilterState
}
