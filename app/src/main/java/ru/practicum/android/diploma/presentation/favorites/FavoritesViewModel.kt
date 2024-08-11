package ru.practicum.android.diploma.presentation.favorites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.favorites.FavoritesInteractor
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.VacancyLight
import ru.practicum.android.diploma.presentation.favorites.state.FavoritesState

class FavoritesViewModel(private val favoritesInteractor: FavoritesInteractor) : ViewModel() {
    private var screenStateLiveData = MutableLiveData<FavoritesState>()
    fun getFavorites() {
        viewModelScope.launch {
            favoritesInteractor.getFavorites().collect { resource ->
                when (resource) {
                    is Resource.Success<*> -> {
                        val castedResource = resource as? Resource.Success<List<VacancyLight>>
                        if (castedResource == null) {
                            renderState(FavoritesState.Error)
                        } else {
                            if (castedResource.data.isEmpty()) {
                                renderState(FavoritesState.Empty)
                            } else {
                                renderState(FavoritesState.Content(castedResource.data.reversed()))
                            }
                        }
                    }

                    is Resource.Error -> {
                        renderState(FavoritesState.Error)
                    }
                }
            }
        }
    }

    private fun renderState(state: FavoritesState) {
        screenStateLiveData.postValue(state)
    }
}
