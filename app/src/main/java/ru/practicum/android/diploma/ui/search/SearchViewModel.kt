package ru.practicum.android.diploma.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.filter.FilterInteractor
import ru.practicum.android.diploma.domain.models.ErrorCode
import ru.practicum.android.diploma.domain.search.SearchInteractor
import ru.practicum.android.diploma.domain.search.entity.ErrorType
import ru.practicum.android.diploma.domain.search.entity.Resource
import ru.practicum.android.diploma.domain.search.entity.SearchState
import ru.practicum.android.diploma.util.debounce

class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val filterInteractor: FilterInteractor
) : ViewModel() {
    private val searchState = MutableLiveData<SearchState>(SearchState.Start)
    fun observeSearchState(): LiveData<SearchState> = searchState
    val searchTextState = MutableLiveData("")
    fun observeSearchTextState(): LiveData<String> = searchTextState
    private var latestSearchText: String? = null
    private val filterEnableState = MutableLiveData<Boolean>()
    fun observeFilterEnableState(): LiveData<Boolean> = filterEnableState

    fun onSearchTextChanged(
        p0: CharSequence?,
        p1: Int,
        p2: Int,
        p3: Int
    ) {
        onTextChangedDebounce(p0.toString())
        if (p0.toString().isEmpty()) {
            searchState.value = SearchState.Start
        }

    }

    fun onEditorActionDone() {
        search(searchTextState.value.toString())
    }

    fun getFilters() {
        filterEnableState.postValue(filterInteractor.isFilterPresent())
    }

    private val onTextChangedDebounce = debounce<String>(
        SEARCH_DEBOUNCE_DELAY,
        viewModelScope,
        true
    ) { text ->
        if (text != latestSearchText && text.isNotEmpty()) {
            latestSearchText = text
            search(text = text)
        }
    }

    private fun search(text: String) {
        if (text.isEmpty() && text != latestSearchText) {
            return
        }

        searchState.postValue(SearchState.Loading)

        viewModelScope.launch {
            searchInteractor.search(
                text = text,
                perPage = PER_PAGE,
                page = PAGE
            ).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        if (resource.data.isNotEmpty()) {
                            searchState.postValue(SearchState.Content(resource.data))
                        } else {
                            searchState.postValue(SearchState.Error(ErrorType.EMPTY))
                        }
                    }

                    is Resource.Error -> {
                        when (resource.code) {
                            ErrorCode.NO_CONNECTION -> searchState.postValue(SearchState.Error(ErrorType.NO_CONNECTION))
                            ErrorCode.BAD_REQUEST -> searchState.postValue(SearchState.Error(ErrorType.SERVER_ERROR))
                        }

                    }
                }
            }
        }
    }

    fun onClearText() {
        clear()
    }

    private fun clear() {
        latestSearchText = ""
        searchTextState.value = ""
        searchState.value = SearchState.Start
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val PER_PAGE = 20
        const val PAGE = 1
    }

}
