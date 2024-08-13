package ru.practicum.android.diploma.presentation.vacancyinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.favorites.FavoritesInteractor
import ru.practicum.android.diploma.domain.vacancyinfo.VacancyInfoInteractor
import ru.practicum.android.diploma.presentation.vacancyinfo.state.VacancyInfoState

class VacancyInfoViewModel(
    private val vacancyInteractor: VacancyInfoInteractor,
    private val favoritesInteractor: FavoritesInteractor
) : ViewModel() {
    private val screenStateLiveData = MutableLiveData<VacancyInfoState>()
    fun getScreenStateLiveData(): LiveData<VacancyInfoState> = screenStateLiveData
    fun searchVacancyInfo(id: String) {
        viewModelScope.launch {
            vacancyInteractor.loadVacancy(id).collect { resource ->
                if (!resource.error && resource.data != null) {
                    renderState(VacancyInfoState.Content(resource.data))
                } else {
                    renderState(VacancyInfoState.Error)
                }
            }
        }
    }

    private fun renderState(state: VacancyInfoState) {
        screenStateLiveData.postValue(state)
    }
}
