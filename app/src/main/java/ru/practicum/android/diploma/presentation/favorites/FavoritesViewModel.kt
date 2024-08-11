package ru.practicum.android.diploma.presentation.favorites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.favorites.FavoritesInteractor
import ru.practicum.android.diploma.presentation.favorites.state.FavoritesState
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class FavoritesViewModel(private val favoritesInteractor: FavoritesInteractor) : ViewModel() {
    private var screenStateLiveData = MutableLiveData<FavoritesState>()
    fun getFavorites() {
        viewModelScope.launch{
            favoritesInteractor.getFavorites().collect{
                if(it.isEmpty()){
                    renderState(FavoritesState.Empty)
                } else{
                    renderState(FavoritesState.Content(it))
                }
            }
        }
    }
    private fun renderState(state: FavoritesState){
        screenStateLiveData.postValue(state)
    }
}
