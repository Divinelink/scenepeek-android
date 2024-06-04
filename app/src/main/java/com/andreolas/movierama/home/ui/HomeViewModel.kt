package com.andreolas.movierama.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreolas.movierama.home.domain.usecase.FetchMultiInfoSearchUseCase
import com.andreolas.movierama.home.domain.usecase.GetFavoriteMoviesUseCase
import com.andreolas.movierama.home.domain.usecase.GetPopularMoviesUseCase
import com.andreolas.movierama.home.domain.usecase.MarkAsFavoriteUseCase
import com.andreolas.movierama.ui.UIText
import com.divinelink.core.commons.domain.data
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.network.media.model.popular.PopularRequestApi
import com.divinelink.core.network.media.model.search.multi.MultiSearchRequestApi
import dagger.hilt.android.lifecycle.HiltViewModel
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
  private val fetchMultiInfoSearchUseCase: FetchMultiInfoSearchUseCase,
  private val markAsFavoriteUseCase: MarkAsFavoriteUseCase,
  private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase,
) : ViewModel() {
  private var currentPage: Int = 1
  private var searchPage: Int = 1

  private var searchJob: Job? = null

  private var allowSearchResult: Boolean = true

  private var latestQuery: String? = null

  // Cached search results is a work in progress. It's not used yet.
  private var cachedSearchResults: HashMap<String, SearchCache> = hashMapOf()

  private val _viewState: MutableStateFlow<HomeViewState> = MutableStateFlow(
    HomeViewState(
      isLoading = true,
      popularMovies = emptyList(),
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
        result.onSuccess {
          val updatedList = getUpdatedMedia(
            currentMediaList = viewState.value.popularMovies,
            updatedMediaList = result.data,
          )
          _viewState.update { viewState ->
            viewState.copy(
              isLoading = false,
              popularMovies = updatedList.filterIsInstance<MediaItem.Media.Movie>(),
            )
          }
        }.onFailure {
          _viewState.update { viewState ->
            viewState.copy(
              isLoading = false,
              error = UIText.StringText(it.message ?: "Something went wrong."),
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
              searchResults = cachedSearchResults[query]?.result,
              emptyResult = cachedSearchResults[query]?.result?.isEmpty() == true,
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
        searchResults = null,
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
      viewState.value.searchResults ?: emptyList()
    }
    latestQuery = query

    viewModelScope.launch {
      _viewState.setLoading()

      fetchMultiInfoSearchUseCase.invoke(
        parameters = MultiSearchRequestApi(
          query = query,
          page = page,
        )
      )
        .distinctUntilChanged()
        .collectLatest { result ->
          result.onSuccess {
            if (allowSearchResult && result.data.query == latestQuery) {
              _viewState.update { viewState ->
                val updatedSearchList = getUpdatedMedia(
                  currentMediaList = currentMoviesList,
                  updatedMediaList = result.data.searchList,
                )/*.also { updatedSearchList -> TODO: Implement caching
                    updateSearchCaches(query, page, updatedSearchList)
                  }*/

                viewState.copy(
                  searchLoadingIndicator = false,
                  isLoading = false,
                  // cachedSearchResults[query]?.result,
                  searchResults = updatedSearchList,
                  // cachedSearchResults[query]?.result?.isEmpty() == true,
                  emptyResult = updatedSearchList.isEmpty(),
                )
              }
            }
          }.onFailure {
            _viewState.update { viewState ->
              viewState.copy(
                searchLoadingIndicator = false,
                error = UIText.StringText(it.message ?: "Something went wrong."),
              )
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
  /*
  private fun updateSearchCaches(
    query: String,
    page: Int,
    searchList: List<MediaItem>,
  ) {
    val cacheList = cachedSearchResults[query]?.result ?: emptyList()
    cachedSearchResults[query] = SearchCache(
      page = page,
      result = getUpdatedMedia(
        currentMediaList = cacheList,
        updatedMediaList = searchList,
      ).toMutableList()
    )
  }
   */

  private fun getUpdatedMedia(
    currentMediaList: List<MediaItem>,
    updatedMediaList: List<MediaItem>,
  ): List<MediaItem> {
    val combinedList = currentMediaList.plus(updatedMediaList).distinctBy { it.id }
    val updatedList = combinedList.toMutableList()
    updatedMediaList.forEach { updatedMovie ->
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
        filters = HomeFilter.entries.map { it.filter },
        filteredResults = null,
      )
    }
  }

  fun onFilterClicked(filter: String) {
    val homeFilter = HomeFilter.entries.find { it.filter.name == filter }
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
        if (result.isSuccess) {
          if (viewState.value.showFavorites == true) {
            _viewState.update { viewState ->
              viewState.copy(
                filteredResults = result.data,
              )
            }
          } else {
            _viewState.update { viewState ->
              viewState.copy(
                filteredResults = viewState.filteredResults?.minus((result.data.toSet()).toSet()),
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
}

data class SearchCache(
  var page: Int,
  var result: MutableList<MediaItem>,
)

private fun MutableStateFlow<HomeViewState>.setLoading() {
  update { viewState ->
    viewState.copy(isLoading = true)
  }
}
