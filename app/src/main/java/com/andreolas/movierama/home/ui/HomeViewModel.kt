package com.andreolas.movierama.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreolas.movierama.base.data.remote.movies.dto.popular.PopularRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.search.SearchRequestApi
import com.andreolas.movierama.home.domain.model.PopularMovie
import com.andreolas.movierama.home.domain.usecase.GetFavoriteMoviesUseCase
import com.andreolas.movierama.home.domain.usecase.GetPopularMoviesUseCase
import com.andreolas.movierama.home.domain.usecase.GetSearchMoviesUseCase
import com.andreolas.movierama.home.domain.usecase.MarkAsFavoriteUseCase
import com.andreolas.movierama.ui.UIText
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.divinelink.core.util.domain.Result
import gr.divinelink.core.util.domain.data
import gr.divinelink.core.util.domain.succeeded
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
  private val getSearchMoviesUseCase: GetSearchMoviesUseCase,
  private val markAsFavoriteUseCase: MarkAsFavoriteUseCase,
  private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase,
) : ViewModel() {
  private var currentPage: Int = 1
  private var searchPage: Int = 1

  private var searchJob: Job? = null
  private var bottomSheetJob: Job? = null

  private var allowSearchResult: Boolean = true

  private var latestQuery: String? = null
  private var cachedSearchResults: HashMap<String, SearchCache> = hashMapOf()

  private val _viewState: MutableStateFlow<HomeViewState> = MutableStateFlow(
    HomeViewState(
      isLoading = true,
      popularMovies = emptyList(),
      selectedMovie = null,
      error = null,
    )
  )
  val viewState: StateFlow<HomeViewState> = _viewState

  init {
    fetchPopularMovies()
  }

  private fun fetchPopularMovies() {
    viewModelScope.launch {
      _viewState.setLoading()

      getPopularMoviesUseCase.invoke(
        parameters = PopularRequestApi(
          page = currentPage,
        )
      ).collectLatest { result ->
        when (result) {
          is Result.Success -> {
            val updatedList = getUpdatedMovies(
              currentMoviesList = viewState.value.popularMovies,
              updatedMoviesList = result.data,
            )
            _viewState.update { viewState ->
              viewState.copy(
                isLoading = false,
                popularMovies = updatedList,
                selectedMovie = updatedSelectedMovie(updatedList, viewState.selectedMovie),
              )
            }
          }
          is Result.Error -> {
            _viewState.update { viewState ->
              viewState.copy(
                isLoading = false,
                error = UIText.StringText(result.exception.message ?: "Something went wrong."),
              )
            }
          }
          Result.Loading -> {
            _viewState.update { viewState ->
              viewState.copy(
                isLoading = true,
              )
            }
          }
        }
      }
    }
  }

  fun onMovieClicked(movie: PopularMovie) {
    bottomSheetJob?.cancel()

    bottomSheetJob = viewModelScope.launch {
      delay(BOTTOM_SHEET_DEBOUNCE_TIME)

      val selectedMovie = if (movie == viewState.value.selectedMovie) {
        null
      } else {
        movie
      }

      _viewState.update { viewState ->
        viewState.copy(selectedMovie = selectedMovie)
      }
    }
  }

  fun onMarkAsFavoriteClicked(movie: PopularMovie) {
    viewModelScope.launch {
      markAsFavoriteUseCase(movie)
    }
  }

  /**
   * Checks whether to load more popularMovies movies,
   * or make a search query with incremented page.
   * If there are filters selected, it will not load more movies.
   */
  fun onLoadNextPage() {
    if (viewState.value.hasFiltersSelected) return

    if (viewState.value.loadMorePopular) {
      currentPage++
      fetchPopularMovies()
    } else {
      // load next page for searching
      searchPage++
      fetchFromSearchQuery(
        query = viewState.value.query,
        page = searchPage,
      )
    }
  }

  fun onSearchMovies(query: String) {
    searchJob?.cancel()
    searchPage = 1
    allowSearchResult = true
    if (query.isEmpty()) {
      onClearClicked()
    } else {
      _viewState.update { viewState ->
        viewState.copy(
          query = query,
          loadMorePopular = false,
          searchLoadingIndicator = true,
        )
      }
      searchJob = viewModelScope.launch {
        delay(timeMillis = 300)
        if (cachedSearchResults.contains(query) && searchPage == 1) {
          Timber.d("Fetching cached results")
          _viewState.update { viewState ->
            latestQuery = query
            viewState.copy(
              isLoading = false,
              searchMovies = cachedSearchResults[query]?.result,
              emptyResult = cachedSearchResults[query]?.result?.isEmpty() == true,
              selectedMovie = null, // updatedSelectedMovie(movies, viewState.selectedMovie)
            )
          }
          // If cache found, set search page to last cached search page
          searchPage = cachedSearchResults[query]?.page ?: 1
          Timber.d("Setting page to: $searchPage")
        } else {
          Timber.d("Fetching data from web service..")
          fetchFromSearchQuery(query = query, page = 1)
        }
      }
    }
  }

  fun onClearClicked() {
    searchJob?.cancel()
    searchPage = 1
    allowSearchResult = false
    _viewState.update { viewState ->
      viewState.copy(
        searchMovies = null,
        loadMorePopular = true,
        query = "",
        emptyResult = false,
        isLoading = false,
        searchLoadingIndicator = false,
      )
    }
  }

  private fun fetchFromSearchQuery(
    query: String,
    page: Int,
  ) {
    val currentMoviesList = if (query != latestQuery) {
      emptyList()
    } else {
      viewState.value.searchMovies ?: emptyList()
    }
    latestQuery = query

    viewModelScope.launch {
      _viewState.setLoading()

      getSearchMoviesUseCase.invoke(
        parameters = SearchRequestApi(
          query = query,
          page = page,
        )
      ).distinctUntilChanged()
        .collectLatest { result ->
          when (result) {
            Result.Loading -> {
              _viewState.update { viewState ->
                viewState.copy(
                  searchLoadingIndicator = true,
                )
              }
            }

            is Result.Success -> {
              if (
                allowSearchResult &&
                result.data.query == latestQuery
              ) {
                _viewState.update { viewState ->
                  val updatedSearchList = getUpdatedMovies(
                    currentMoviesList = currentMoviesList,
                    updatedMoviesList = result.data.searchList,
                  ).also { updatedSearchList ->
                    // Fix caching
                    // updateSearchCaches(query, page, updatedSearchList)
                  }

                  viewState.copy(
                    searchLoadingIndicator = false,
                    isLoading = false,
                    searchMovies = updatedSearchList, // cachedSearchResults[query]?.result,
                    emptyResult = updatedSearchList.isEmpty(), // cachedSearchResults[query]?.result?.isEmpty() == true,
                    selectedMovie = updatedSearchList.find { it.id == viewState.selectedMovie?.id },
                  )
                }
              }
            }

            is Result.Error -> {
              _viewState.update { viewState ->
                viewState.copy(
                  searchLoadingIndicator = false,
                  error = UIText.StringText(result.exception.message ?: "Something went wrong."),
                )
              }
            }
          }
        }
    }
  }

  /**
   * @param [query] Current query the user has executed.
   * @param [page] Page of the current query.
   * @param [searchList] A list containing the updated version of all search movies lastly emitted.
   * * This method updates the cached search results given a [query].
   * It appends to the current caches a list of movies that has been emitted and also updates the last page of the query.
   */
  @Suppress("UnusedPrivateMember")
  private fun updateSearchCaches(
    query: String,
    page: Int,
    searchList: List<PopularMovie>,
  ) {
    val cacheList = cachedSearchResults[query]?.result ?: emptyList()
    cachedSearchResults[query] = SearchCache(
      page = page,
      result = getUpdatedMovies(
        currentMoviesList = cacheList,
        updatedMoviesList = searchList,
      ).toMutableList()
    )
  }

  /**
   * Update selected movie if exists on Popular Movies or in Search Movies List.
   */
  private fun updatedSelectedMovie(
    updatedList: List<PopularMovie>,
    selectedMovie: PopularMovie?,
  ): PopularMovie? {
    return updatedList
      .find { it.id == selectedMovie?.id } ?: viewState.value.searchMovies
      ?.find { it.id == selectedMovie?.id }
  }

  private fun getUpdatedMovies(
    currentMoviesList: List<PopularMovie>,
    updatedMoviesList: List<PopularMovie>,
  ): List<PopularMovie> {
    val combinedList = currentMoviesList.plus(updatedMoviesList).distinctBy { it.id }
    val updatedList = combinedList.toMutableList()
    updatedMoviesList.forEach { updatedMovie ->
      val index = updatedList.indexOfFirst { it.id == updatedMovie.id }
      if (index != -1) {
        updatedList[index] = updatedMovie
      }
    }
    return updatedList.distinctBy { it.id }
  }

  fun onClearFiltersClicked() {
    _viewState.update { viewState ->
      viewState.copy(
        filters = HomeFilter.values().map { it.filter },
        filteredMovies = null,
      )
    }
  }

  fun onSwipeDown() {
    viewModelScope.launch {
      delay(BOTTOM_SHEET_DEBOUNCE_TIME)
      _viewState.update { viewState ->
        viewState.copy(
          selectedMovie = null,
        )
      }
    }
  }

  fun onFilterClicked(filter: String) {
    val homeFilter = HomeFilter.values().find { it.filter.name == filter }
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

  /**
   * Handles the filters for the liked movies.
   * This method fetches the liked movies from the database and updates the view state.
   */
  private fun updateLikedFilteredMovies() {
    getFavoriteMoviesUseCase(Unit)
      .onEach { result ->
        if (result.succeeded) {
          if (viewState.value.showFavorites == true) {
            _viewState.update { viewState ->
              viewState.copy(
                filteredMovies = result.data!!,
              )
            }
          } else {
            _viewState.update { viewState ->
              viewState.copy(
                filteredMovies = viewState.filteredMovies?.minus((result.data!!.toSet()).toSet()),
              )
            }
          }
        }
      }
      .launchIn(viewModelScope)
  }

  /**
   * Updates the filters list.
   * @param [homeFilter] The filter to be updated.
   * This method updates the filters list by toggling the selected state of the filter.
   * If the filter is already selected, it will be unselected and vice versa.
   */
  private fun updateFilters(homeFilter: HomeFilter?) {
    _viewState.update { viewState ->
      viewState.copy(
        filters = viewState.filters.map { currentFilter ->
          if (currentFilter.name == homeFilter?.filter?.name) {
            currentFilter.copy(isSelected = !currentFilter.isSelected)
          } else {
            currentFilter
          }
        },
      )
    }
  }

  companion object {
    const val BOTTOM_SHEET_DEBOUNCE_TIME = 200L
  }
}

data class SearchCache(
  var page: Int,
  var result: MutableList<PopularMovie>,
)

private fun MutableStateFlow<HomeViewState>.setLoading() {
  update { viewState ->
    viewState.copy(isLoading = true)
  }
}
