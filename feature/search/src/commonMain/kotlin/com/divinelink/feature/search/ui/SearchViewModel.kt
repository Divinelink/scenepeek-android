package com.divinelink.feature.search.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.domain.search.FetchMultiInfoSearchUseCase
import com.divinelink.core.domain.search.MultiSearchParameters
import com.divinelink.core.domain.search.MultiSearchResult
import com.divinelink.core.domain.search.SearchStateManager
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.search.SearchEntryPoint
import com.divinelink.core.model.tab.SearchTab
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
  private val fetchMultiInfoSearchUseCase: FetchMultiInfoSearchUseCase,
  private val searchStateManager: SearchStateManager,
) : ViewModel() {

  private val _uiState: MutableStateFlow<SearchUiState> = MutableStateFlow(SearchUiState.initial())
  val uiState: StateFlow<SearchUiState> = _uiState

  private var searchJob: Job? = null

  init {
    viewModelScope.launch {
      searchStateManager.entryPoint.collectLatest { entryPoint ->
        when (entryPoint) {
          SearchEntryPoint.HOME -> _uiState.update { uiState ->
            uiState.copy(focusSearch = true)
          }
          SearchEntryPoint.SEARCH_TAB -> _uiState.update { uiState ->
            uiState.copy(focusSearch = false)
          }
        }
      }
    }
  }

  fun updateEntryPoint() {
    searchStateManager.updateEntryPoint(SearchEntryPoint.SEARCH_TAB)
  }

  fun onSearch(
    query: String,
    reset: Boolean,
  ) {
    searchJob?.cancel()

    if (reset) {
      resetPage()
    }

    if (query.isEmpty()) {
      onClearClick()
    } else {
      _uiState.update { viewState ->
        viewState.copy(
          query = query,
          isLoading = true,
        )
      }
      searchJob = viewModelScope.launch {
        delay(timeMillis = 300)

        fetchFromSearchQuery(
          query = query,
          page = 1,
          tab = SearchTab.fromIndex(uiState.value.selectedTabIndex),
        )
      }
    }
  }

  private fun fetchFromSearchQuery(
    tab: SearchTab,
    query: String,
    page: Int,
  ) {
    val isNewSearch = query != uiState.value.lastQuery[tab]

    _uiState.update { uiState ->
      uiState.copy(
        lastQuery = uiState.lastQuery + (tab to query),
      )
    }

    viewModelScope.launch {
      fetchMultiInfoSearchUseCase.invoke(
        parameters = MultiSearchParameters(
          query = query,
          page = page,
          tab = tab,
        ),
      )
        .takeWhile { uiState.value.query == query }
        .distinctUntilChanged()
        .collect { result ->
          result.fold(
            onSuccess = { data ->
              handleSearchSuccess(
                reset = isNewSearch,
                response = data,
              )
            },
            onFailure = {
              handleSearchError(
                reset = isNewSearch,
                tab = tab,
                error = it,
              )
            },
          )
        }
    }
  }

  private fun handleSearchSuccess(
    reset: Boolean,
    response: MultiSearchResult,
  ) {
    _uiState.update { uiState ->
      val data = (uiState.forms[response.tab] as? SearchForm.Data)?.pages ?: mapOf(
        1 to response.searchList,
      )

      uiState.copy(
        forms = uiState.forms.plus(
          response.tab to SearchForm.Data(
            pages = if (reset) {
              mapOf(1 to response.searchList)
            } else {
              data.plus(response.page to response.searchList)
            },
          ),
        ),
        pages = uiState.pages + (response.tab to response.page),
        canFetchMore = uiState.canFetchMore + (response.tab to response.canFetchMore),
        isLoading = false,
      )
    }
  }

  fun onClearClick() {
    searchJob?.cancel()
    resetPage()
    _uiState.update { uiState ->
      uiState.copy(
        forms = SearchTab.entries.associateWith { tab ->
          when (tab) {
            SearchTab.All -> SearchForm.Initial
            SearchTab.Movie -> SearchForm.Initial
            SearchTab.People -> SearchForm.Initial
            SearchTab.TV -> SearchForm.Initial
          }
        },
        canFetchMore = SearchTab.entries.associateWith { tab ->
          when (tab) {
            SearchTab.All -> true
            SearchTab.Movie -> true
            SearchTab.People -> true
            SearchTab.TV -> true
          }
        },
        lastQuery = SearchTab.entries.associateWith { tab ->
          when (tab) {
            SearchTab.All -> null
            SearchTab.Movie -> null
            SearchTab.People -> null
            SearchTab.TV -> null
          }
        },
        query = "",
        isLoading = false,
        focusSearch = false,
      )
    }
  }

  fun onLoadNextPage() {
    fetchFromSearchQuery(
      query = uiState.value.query,
      page = uiState.value.pages[uiState.value.selectedTab]?.plus(1) ?: 1,
      tab = uiState.value.selectedTab,
    )
  }

  fun onRetryClick() {
    resetLastQuery()
    onSearch(
      query = uiState.value.query,
      reset = true,
    )
  }

  fun onSelectTab(tab: SearchTab) {
    _uiState.update { uiState ->
      uiState.copy(
        selectedTabIndex = tab.order,
        forms = uiState.forms.keys.associateWith { formTab ->
          val tabsLastQuery = uiState.lastQuery[formTab]
          if (formTab == tab && tabsLastQuery != uiState.query) {
            SearchForm.Loading
          } else {
            uiState.forms[formTab] ?: SearchForm.Initial
          }
        },
      )
    }

    if (uiState.value.lastQuery[tab] != uiState.value.query) {
      onSearch(
        query = uiState.value.query,
        reset = false,
      )
    }
  }

  private fun resetPage() {
    _uiState.update { uiState ->
      uiState.copy(
        pages = SearchTab.entries.associateWith { tab ->
          when (tab) {
            SearchTab.All -> 0
            SearchTab.Movie -> 0
            SearchTab.People -> 0
            SearchTab.TV -> 0
          }
        },
      )
    }
  }

  private fun resetLastQuery() {
    _uiState.update { uiState ->
      uiState.copy(
        lastQuery = SearchTab.entries.associateWith { tab ->
          when (tab) {
            SearchTab.All -> null
            SearchTab.Movie -> null
            SearchTab.People -> null
            SearchTab.TV -> null
          }
        },
      )
    }
  }

  private fun handleSearchError(
    reset: Boolean,
    tab: SearchTab,
    error: Throwable,
  ) {
    _uiState.update { uiState ->
      uiState.copy(
        forms = if (uiState.forms[tab] !is SearchForm.Data || reset) {
          uiState.forms.plus(
            tab to when (error) {
              is AppException.Offline -> SearchForm.Error.Network
              else -> SearchForm.Error.Unknown
            },
          )
        } else {
          uiState.forms
        },
        isLoading = false,
      )
    }
  }
}
