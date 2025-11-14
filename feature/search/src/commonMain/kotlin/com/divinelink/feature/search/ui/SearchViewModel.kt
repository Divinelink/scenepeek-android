package com.divinelink.feature.search.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.commons.data
import com.divinelink.core.domain.MarkAsFavoriteUseCase
import com.divinelink.core.domain.search.FetchMultiInfoSearchUseCase
import com.divinelink.core.domain.search.MultiSearchParameters
import com.divinelink.core.domain.search.SearchStateManager
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaSection
import com.divinelink.core.model.search.SearchEntryPoint
import com.divinelink.core.ui.blankslate.BlankSlateState
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
    var isNewSearch = query != latestQuery
    latestQuery = query

    viewModelScope.launch {
      fetchMultiInfoSearchUseCase.invoke(
        parameters = MultiSearchParameters(
          query = query,
          page = page,
        ),
      )
        .distinctUntilChanged()
        .collectLatest { result ->
          result.onSuccess {
            incrementPage()
            if (result.data.query == latestQuery) {
              _uiState.update { viewState ->
                viewState.copy(
                  isLoading = false,
                  error = null,
                  searchResults = if (isNewSearch) {
                    isNewSearch = false
                    MediaSection(
                      data = result.data.searchList,
                      shouldLoadMore = result.data.totalPages > page,
                    )
                  } else {
                    viewState.searchResults
                      ?.addMore(result.data.searchList)
                      ?.copy(shouldLoadMore = result.data.totalPages > page)
                  },
                )
              }
            }
          }.onFailure {
            handleSearchError(it)
          }
        }
    }
  }

  fun onClearClick() {
    searchJob?.cancel()
    resetPage()
    latestQuery = null
    _uiState.update { viewState ->
      viewState.copy(
        searchResults = null,
        query = "",
        isLoading = false,
        focusSearch = false,
      )
    }
  }

  fun onLoadNextPage() {
    fetchFromSearchQuery(
      query = uiState.value.query,
      page = uiState.value.page,
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

  private fun incrementPage() {
    _uiState.update { uiState ->
      uiState.copy(
        page = uiState.page + 1,
      )
    }
  }

  private fun resetPage() {
    _uiState.update { viewState ->
      viewState.copy(
        page = 1,
      )
    }
  }

  private fun handleSearchError(it: Throwable) {
    _uiState.update { viewState ->
      viewState.copy(
        isLoading = false,
      )
    }

    if (it is AppException.Offline) {
      if (uiState.value.page == 1) {
        _uiState.update { viewState ->
          viewState.copy(
            searchResults = null,
            error = BlankSlateState.Offline,
          )
        }
      }
    }
  }
}
