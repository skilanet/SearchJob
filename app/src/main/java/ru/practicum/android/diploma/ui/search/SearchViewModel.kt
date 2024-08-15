package ru.practicum.android.diploma.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.search.SearchInteractor
import ru.practicum.android.diploma.domain.search.entity.ErrorType
import ru.practicum.android.diploma.domain.search.entity.SearchState
import ru.practicum.android.diploma.util.debounce

class SearchViewModel(private val searchInteractor: SearchInteractor) : ViewModel() {
    private val searchState = MutableLiveData<SearchState>(SearchState.Start)
    fun observeSearchState(): LiveData<SearchState> = searchState
    val searchTextState = MutableLiveData<String>()
    private var latestSearchText: String? = null

    fun onSearchTextChanged(
        p0: CharSequence?,
        p1: Int,
        p2: Int,
        p3: Int
    ) {
        onTextChangedDebounce(p0.toString())
    }

    fun onEditorActionDone(text: String) {
        onTextChangedDebounce(searchTextState.value.toString())
    }

    val onTextChangedDebounce = debounce<String>(
        SEARCH_DEBOUNCE_DELAY,
        viewModelScope,
        true
    ) { text ->
        if (text != latestSearchText) {
            latestSearchText = text
            search(text = text)
        }
    }

    private fun search(text: String) {
        if (text.isEmpty()) {
            return
        }

        viewModelScope.launch {
            searchInteractor.search(
                filter = null,
                text = text
            )
                .cachedIn(viewModelScope)
                .catch {
                    searchState.postValue(SearchState.Error(ErrorType.SERVER_ERROR))
                }
                .combine(searchInteractor.totalFoundFlow) { pagingData, total ->
                    pagingData to total
                }
                .collect { data ->
                    searchState.postValue(SearchState.Content(data.first, data.second))
                }
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
