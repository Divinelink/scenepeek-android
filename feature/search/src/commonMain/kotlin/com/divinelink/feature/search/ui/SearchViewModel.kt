package com.divinelink.feature.search.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.domain.MarkAsFavoriteUseCase
import com.divinelink.core.domain.search.FetchMultiInfoSearchUseCase
import com.divinelink.core.domain.search.MultiSearchParameters
import com.divinelink.core.domain.search.MultiSearchResult
import com.divinelink.core.domain.search.SearchStateManager
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.search.SearchEntryPoint
import com.divinelink.core.model.tab.SearchTab
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
  private val fetchMultiInfoSearchUseCase: FetchMultiInfoSearchUseCase,
  private val markAsFavoriteUseCase: MarkAsFavoriteUseCase,
  private val searchStateManager: SearchStateManager,
) : ViewModel() {

  private val _uiState: MutableStateFlow<SearchUiState> = MutableStateFlow(SearchUiState.initial())
  val uiState: StateFlow<SearchUiState> = _uiState

  private var searchJob: Job? = null

  private var latestQuery: String? = null

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

  fun onSearchMovies(query: String) {
    searchJob?.cancel()
    resetPage()
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

        fetchFromSearchQuery(query = query, page = 1)
      }
    }
  }

  private fun fetchFromSearchQuery(
    query: String,
    page: Int,
  ) {
    val isNewSearch = query != latestQuery
    latestQuery = query
    val tab = uiState.value.tabs[uiState.value.selectedTabIndex]

    viewModelScope.launch {
      fetchMultiInfoSearchUseCase.invoke(
        parameters = MultiSearchParameters(
          query = query,
          page = page,
          tab = tab,
        ),
      )
        .distinctUntilChanged()
        .collectLatest { result ->
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
      val data = (uiState.forms[response.tab] as? SearchForm.Data)?.paginationData ?: mapOf(
        1 to response.searchList,
      )

      uiState.copy(
        forms = uiState.forms.plus(
          response.tab to SearchForm.Data(
            tab = response.tab,
            paginationData = if (reset) {
              mapOf(1 to response.searchList)
            } else {
              data.plus(response.page to response.searchList)
            },
          ),
        ),
        pages = uiState.pages + (response.tab to response.page + 1),
        canFetchMore = uiState.canFetchMore + (response.tab to response.canFetchMore),
        isLoading = false,
      )
    }
  }


  fun onClearClick() {
    searchJob?.cancel()
    resetPage()
    latestQuery = null
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
        query = "",
        isLoading = false,
        focusSearch = false,
      )
    }
  }

  fun onLoadNextPage() {
    fetchFromSearchQuery(
      query = uiState.value.query,
      page = uiState.value.pages[uiState.value.selectedTab] ?: 1,
    )
  }

  fun onMarkAsFavoriteClick(movie: MediaItem) {
    if (movie !is MediaItem.Media) return

    viewModelScope.launch {
      markAsFavoriteUseCase(movie)
    }
  }

  fun onRetryClick() {
    latestQuery = null
    onSearchMovies(uiState.value.query)
  }

  fun onSelectTab(index: Int) {
    _uiState.update { uiState ->
      uiState.copy(selectedTabIndex = index)
    }
  }

  private fun resetPage() {
    _uiState.update { uiState ->
      uiState.copy(
        pages = SearchTab.entries.associateWith { tab ->
          when (tab) {
            SearchTab.All -> 1
            SearchTab.Movie -> 1
            SearchTab.People -> 1
            SearchTab.TV -> 1
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
