package ru.practicum.android.diploma.presentation.vacancyinfo.state

import ru.practicum.android.diploma.domain.models.VacancyFull

sealed class VacancyInfoState {
    data object Error : VacancyInfoState()
    data object Loading : VacancyInfoState()
    data class Content(val vacancy: VacancyFull) : VacancyInfoState()
}
