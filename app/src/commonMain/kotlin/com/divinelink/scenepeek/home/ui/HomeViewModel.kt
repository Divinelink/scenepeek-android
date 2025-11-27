package com.divinelink.scenepeek.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.commons.data
import com.divinelink.core.commons.onError
import com.divinelink.core.domain.MarkAsFavoriteUseCase
import com.divinelink.core.domain.search.SearchStateManager
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.home.HomeMode
import com.divinelink.core.model.home.HomePage
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaSection
import com.divinelink.core.model.search.SearchEntryPoint
import com.divinelink.core.network.media.model.movie.MoviesRequestApi
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.core.ui.components.Filter
import com.divinelink.core.domain.GetFavoriteMoviesUseCase
import com.divinelink.core.domain.GetPopularMoviesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
  private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
  private val markAsFavoriteUseCase: MarkAsFavoriteUseCase,
  private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase,
  private val searchStateManager: SearchStateManager,
) : ViewModel() {

  private val _viewState: MutableStateFlow<HomeViewState> = MutableStateFlow(
    HomeViewState.initial(),
  )
  val viewState: StateFlow<HomeViewState> = _viewState

  init {
    fetchPopularMovies()
  }

  private fun fetchPopularMovies() {
    if (getPage(HomePage.Popular) == 1) {
      _viewState.setLoading()
    }

    viewModelScope.launch {
      getPopularMoviesUseCase.invoke(
        parameters = MoviesRequestApi(
          page = getPage(HomePage.Popular),
        ),
      ).collectLatest { result ->
        result.onSuccess {
          incrementPage(HomePage.Popular)
          _viewState.update { viewState ->
            viewState.copy(
              isLoading = false,
              error = null,
              retryAction = null,
              popularMovies = viewState.popularMovies.addMore(result.data),
            )
          }
        }.onError<AppException.Offline> {
          if (getPage(HomePage.Popular) == 1) {
            _viewState.update { viewState ->
              viewState.copy(
                error = BlankSlateState.Offline,
                retryAction = HomeMode.Browser,
                isLoading = false,
              )
            }
          }
        }.onFailure {
          _viewState.update { viewState ->
            viewState.copy(
              isLoading = false,
              error = BlankSlateState.Generic,
            )
          }
        }
      }
    }
  }

  fun onMarkAsFavoriteClicked(movie: MediaItem) {
    if (movie !is MediaItem.Media) return

    viewModelScope.launch {
      markAsFavoriteUseCase(movie)
    }
  }

  /**
   * Checks whether to load more popularMovies movies,
   * or make a search query with incremented page.
   * If there are language selected, it will not load more movies.
   */
  fun onLoadNextPage() {
    when (viewState.value.mode) {
      HomeMode.Filtered -> return
      HomeMode.Browser -> if (viewState.value.popularMovies.shouldLoadMore) {
        fetchPopularMovies()
      }
    }
  }

  fun onClearFiltersClicked() {
    _viewState.update { viewState ->
      viewState.copy(
        filters = HomeFilter.entries.map { it.filter },
        filteredResults = null,
        mode = HomeMode.Browser,
      )
    }
  }

  fun onFilterClick(filter: Filter) {
    val homeFilter = HomeFilter.entries.find { it.filter.name == filter.name }
    updateFilters(homeFilter)

    when (homeFilter) {
      HomeFilter.Liked -> {
        updateLikedFilteredMovies()
      }

      else -> {
        // Do nothing
      }
    }
  }

  fun onRetryClick() {
    when (viewState.value.retryAction) {
      HomeMode.Browser -> fetchPopularMovies()
      else -> {
        // Do nothing
      }
    }
  }

  fun onNavigateToSearch() {
    searchStateManager.updateEntryPoint(SearchEntryPoint.HOME)
  }

  /**
   * Handles the language for the liked movies.
   * This method fetches the liked movies from the database and updates the view state.
   */
  private fun updateLikedFilteredMovies() {
    getFavoriteMoviesUseCase(Unit)
      .onEach { result ->
        if (result.isSuccess) {
          if (viewState.value.showFavorites == true) {
            _viewState.update { viewState ->
              viewState.copy(
                filteredResults = MediaSection(data = result.data, shouldLoadMore = false),
              )
            }
          } else {
            _viewState.update { viewState ->
              viewState.copy(
                filteredResults = MediaSection(
                  data = viewState.filteredResults?.data?.minus((result.data.toSet()).toSet())
                    ?: emptyList(),
                  shouldLoadMore = false,
                ),
              )
            }
          }
        }
      }
      .launchIn(viewModelScope)
  }

  /**
   * Updates the language list.
   * @param [homeFilter] The filter to be updated.
   * This method updates the language list by toggling the selected state of the filter.
   * If the filter is already selected, it will be unselected and vice versa.
   */
  private fun updateFilters(homeFilter: HomeFilter?) {
    _viewState.update { viewState ->
      viewState.copy(
        // TODO add test
        retryAction = null,
        filters = viewState.filters.map { currentFilter ->
          if (currentFilter.name == homeFilter?.filter?.name) {
            currentFilter.copy(isSelected = !currentFilter.isSelected)
          } else {
            currentFilter
          }
        },
      )
    }
    _viewState.update {
      it.copy(
        mode = if (viewState.value.filters.any { filter -> filter.isSelected }) {
          HomeMode.Filtered
        } else {
          HomeMode.Browser
        },
      )
    }
  }

  private fun incrementPage(page: HomePage) {
    _viewState.update { viewState ->
      viewState.copy(
        pages = viewState.pages + (page to (viewState.pages[page] ?: 1) + 1),
      )
    }
  }

  private fun getPage(page: HomePage): Int = viewState.value.pages[page] ?: 1
}

private fun MutableStateFlow<HomeViewState>.setLoading() {
  update { viewState ->
    viewState.copy(isLoading = true)
  }
}
