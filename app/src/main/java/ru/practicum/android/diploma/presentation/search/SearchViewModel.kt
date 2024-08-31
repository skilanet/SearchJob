package ru.practicum.android.diploma.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.filter.FilterInteractor
import ru.practicum.android.diploma.domain.models.ErrorCode
import ru.practicum.android.diploma.domain.models.VacancyLight
import ru.practicum.android.diploma.domain.search.SearchInteractor
import ru.practicum.android.diploma.domain.search.entity.ErrorType
import ru.practicum.android.diploma.domain.search.entity.Resource
import ru.practicum.android.diploma.presentation.search.state.SearchState
import ru.practicum.android.diploma.util.SingleEventLiveData
import ru.practicum.android.diploma.util.debounce

class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val filterInteractor: FilterInteractor
) : ViewModel() {
    private val searchState = MutableLiveData<SearchState>(SearchState.Start)
    val searchTextState = MutableLiveData("")
    private val filterEnableState = SingleEventLiveData<Boolean>()
    private val totalFoundLiveData = MutableLiveData<Int>()
    private val pagingError = SingleEventLiveData<Boolean>()

    fun observeSearchTextState(): LiveData<String> = searchTextState
    fun observeSearchState(): LiveData<SearchState> = searchState
    fun observeFilterEnableState(): LiveData<Boolean> = filterEnableState
    fun observeTotalFoundLiveData(): LiveData<Int> = totalFoundLiveData
    fun observePagingErrorEvent(): LiveData<Boolean> = pagingError

    private var latestSearchText: String? = null
    private var currentPage: Int = 0
    private var maxPages: Int? = null
    private var isNextPageLoading: Boolean = false
    private var searching: Boolean = false
    private val vacancyList = mutableListOf<VacancyLight>()

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
        if (searchTextState.value.toString() != latestSearchText) {
            latestSearchText = searchTextState.value
            search(searchTextState.value.toString())
        }
    }

    fun getFilters() {
        filterEnableState.postValue(filterInteractor.isFilterPresent())
    }

    fun checkFilterApplyAndSearch() {
        viewModelScope.launch {
            if (filterInteractor.readFilterApplicationSetting() && !latestSearchText.isNullOrBlank()) {
                search(searchTextState.value.toString())
                filterInteractor.saveFilterApplicationSetting(false)
            }
        }
    }

    private val onTextChangedDebounce = debounce<String>(
        SEARCH_DEBOUNCE_DELAY,
        viewModelScope,
        true
    ) { text ->
        if (text.isNotBlank() && latestSearchText != text && !searching) {
            latestSearchText = text
            search(text = text)
        }
    }

    private fun search(text: String) {
        if (text.isBlank() || searching) {
            return
        }
        searching = true
        currentPage = 0
        maxPages = null
        vacancyList.clear()
        viewModelScope.launch {
            searchState.postValue(SearchState.Loading)
            searchInteractor.search(text = text, 0)
                .catch {
                    searchState.postValue(SearchState.Error(ErrorType.SERVER_ERROR))
                }
                .collect { resource ->
                    when (resource) {
                        is Resource.Error -> {
                            handleErrorCode(resource.code)
                        }

                        is Resource.Success -> {
                            if (resource.data.isEmpty()) {
                                totalFoundLiveData.value = 0
                                searchState.postValue(SearchState.Error(ErrorType.EMPTY))
                            } else {
                                currentPage = 1
                                maxPages = resource.pages
                                totalFoundLiveData.value = resource.total
                                vacancyList.addAll(resource.data)
                                searchState.postValue(SearchState.Content(vacancyList, false))
                            }
                        }
                    }
                    searching = false
                }
        }
    }

    private fun handleErrorCode(code: Int) {
        when (code) {
            ErrorCode.NO_CONNECTION -> {
                searchState.postValue(SearchState.Error(ErrorType.NO_CONNECTION))
            }

            else -> searchState.postValue(SearchState.Error(ErrorType.SERVER_ERROR))
        }
    }

    fun loadNextPage() {
        if (currentPage != maxPages && maxPages != null && !isNextPageLoading) {
            isNextPageLoading = true
            viewModelScope.launch {
                searchState.postValue(SearchState.PageLoading)
                searchInteractor.search(latestSearchText!!, currentPage).catch {
                    currentPage = 0
                    maxPages = null
                    searchState.postValue(SearchState.Error(ErrorType.SERVER_ERROR))
                }.collect { resource ->
                    when (resource) {
                        is Resource.Error -> {
                            searchState.postValue(SearchState.Content(vacancyList, true))
                            pagingError.postValue(true)
                        }

                        is Resource.Success -> {
                            currentPage += 1
                            vacancyList.addAll(resource.data)
                            searchState.postValue(SearchState.Content(vacancyList, true))
                        }
                    }

                }
                isNextPageLoading = false
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
    }
}
