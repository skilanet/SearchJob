package ru.practicum.android.diploma.presentation.vacancyinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.favorites.FavoritesInteractor
import ru.practicum.android.diploma.domain.models.VacancyFull
import ru.practicum.android.diploma.domain.vacancyinfo.VacancyInfoInteractor
import ru.practicum.android.diploma.presentation.vacancyinfo.state.FavoriteButtonState
import ru.practicum.android.diploma.presentation.vacancyinfo.state.VacancyInfoState

class VacancyInfoViewModel(
    private val vacancyInteractor: VacancyInfoInteractor,
    private val favoritesInteractor: FavoritesInteractor
) : ViewModel() {
    private val screenStateLiveData = MutableLiveData<VacancyInfoState>()
    private val favoriteStateLiveData = MutableLiveData<FavoriteButtonState>()
    private var currentVacancy: VacancyFull? = null
    fun getScreenStateLiveData(): LiveData<VacancyInfoState> = screenStateLiveData
    fun getFavoriteButtonStateLiveData(): LiveData<FavoriteButtonState> = favoriteStateLiveData
    fun searchVacancyInfo(id: String) {
        viewModelScope.launch {
            renderState(VacancyInfoState.Loading)
            vacancyInteractor.loadVacancy(id).collect { resource ->
                if (!resource.error && resource.data != null) {
                    currentVacancy = resource.data
                    renderState(VacancyInfoState.Content(resource.data))
                    favoritesInteractor.checkVacancyInFavorites(resource.data.id).collect { isFavorite ->
                        favoriteStateLiveData.postValue(FavoriteButtonState(isFavorite))
                    }
                } else {
                    renderState(VacancyInfoState.Error)
                }
            }
        }
    }

    fun onShareClick(): String {
        return currentVacancy?.let { it.url ?: it.alternativeUrl } ?: "Отсутствует ссылка на данную вакансию"
    }

    fun onFavoriteClicked() {
        viewModelScope.launch {
            if (currentVacancy != null) {
                favoritesInteractor.checkVacancyInFavorites(currentVacancy!!.id).collect { isFavorite ->
                    if (isFavorite) {
                        favoritesInteractor.removeFromFavorites(currentVacancy!!.id)
                    } else {
                        favoritesInteractor.addToFavorites(currentVacancy!!)
                    }
                    favoriteStateLiveData.postValue(FavoriteButtonState(!isFavorite))
                }
            }
        }
    }

    private fun renderState(state: VacancyInfoState) {
        screenStateLiveData.postValue(state)
    }
}
