package com.andreolas.movierama.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreolas.movierama.home.domain.usecase.FetchMultiInfoSearchUseCase
import com.andreolas.movierama.home.domain.usecase.GetFavoriteMoviesUseCase
import com.andreolas.movierama.home.domain.usecase.GetPopularMoviesUseCase
import com.divinelink.core.commons.domain.data
import com.divinelink.core.domain.MarkAsFavoriteUseCase
import com.divinelink.core.model.home.HomeMode
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.network.media.model.movie.MoviesRequestApi
import com.divinelink.core.network.media.model.search.multi.MultiSearchRequestApi
import com.divinelink.core.ui.UIText
import com.divinelink.core.ui.components.Filter
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

class HomeViewModel(
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
    HomeViewState.initial(),
  )
  val viewState: StateFlow<HomeViewState> = _viewState

  init {
    fetchPopularMovies()
  }

  private fun fetchPopularMovies() {
    viewModelScope.launch {
      getPopularMoviesUseCase.invoke(
        parameters = MoviesRequestApi(
          page = currentPage,
        ),
      ).collectLatest { result ->
        result.onSuccess {
          currentPage++

          _viewState.update { viewState ->
            viewState.copy(
              isLoading = false,
              popularMovies = viewState.popularMovies.addMore(result.data),
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
    when (viewState.value.mode) {
      HomeMode.Filtered -> return
      HomeMode.Browser -> if (viewState.value.popularMovies.shouldLoadMore) {
        fetchPopularMovies()
      }
      HomeMode.Search -> if (viewState.value.searchResults?.shouldLoadMore == true) {
        fetchFromSearchQuery(
          query = viewState.value.query,
          page = searchPage,
        )
      }
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
          isSearchLoading = true,
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
              searchResults = MediaSection(
                data = cachedSearchResults[query]?.result ?: emptyList(),
                shouldLoadMore = true,
              ),
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
    latestQuery = null
    _viewState.update { viewState ->
      viewState.copy(
        searchResults = null,
        query = "",
        isLoading = false,
        isSearchLoading = false,
        mode = if (viewState.filters.any { it.isSelected }) {
          HomeMode.Filtered
        } else {
          HomeMode.Browser
        },
      )
    }
  }

  private fun fetchFromSearchQuery(
    query: String,
    page: Int,
  ) {
    var isNewSearch = query != latestQuery
    latestQuery = query

    viewModelScope.launch {
      _viewState.setLoading()

      fetchMultiInfoSearchUseCase.invoke(
        parameters = MultiSearchRequestApi(
          query = query,
          page = page,
        ),
      )
        .distinctUntilChanged()
        .collectLatest { result ->
          result.onSuccess {
            searchPage++

            if (allowSearchResult && result.data.query == latestQuery) {
              _viewState.update { viewState ->
                viewState.copy(
                  isSearchLoading = false,
                  isLoading = false,
                  mode = HomeMode.Search,
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
            _viewState.update { viewState ->
              viewState.copy(
                isLoading = false,
                isSearchLoading = false,
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
