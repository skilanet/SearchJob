package ru.practicum.android.diploma.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.models.ErrorCode
import ru.practicum.android.diploma.domain.models.VacancyLight
import ru.practicum.android.diploma.domain.search.SearchInteractor
import ru.practicum.android.diploma.domain.search.entity.ErrorType
import ru.practicum.android.diploma.domain.search.entity.Resource
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
            ).collect { pagingData ->
                searchState.postValue(SearchState.Content(pagingData))
            }
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
