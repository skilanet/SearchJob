package ru.practicum.android.diploma.presentation.favorites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.favorites.FavoritesInteractor
import ru.practicum.android.diploma.presentation.favorites.state.FavoritesState

class FavoritesViewModel(private val favoritesInteractor: FavoritesInteractor) : ViewModel() {
    private var screenStateLiveData = MutableLiveData<FavoritesState>()
    fun getFavorites() {
        viewModelScope.launch {
            favoritesInteractor.getFavorites().collect { resource ->
                if(resource.error){
                    renderState(FavoritesState.Error)
                } else{
                    if (resource.data.isEmpty()){
                        renderState(FavoritesState.Empty)
                    } else{
                        renderState(FavoritesState.Content(resource.data))
                    }
                }
            }
        }
    }
    // думаю этот метод тут не нужен так как мы из избранного на экране вакансии только удалять можем если я правильно помню

    fun removeFromFavorites(vacancyId: String) {
        viewModelScope.launch {
            favoritesInteractor.removeFromFavorites(vacancyId)
        }
    }

    private fun renderState(state: FavoritesState) {
        screenStateLiveData.postValue(state)
    }
}
