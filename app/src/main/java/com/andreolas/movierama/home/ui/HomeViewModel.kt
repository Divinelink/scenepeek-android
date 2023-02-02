package com.andreolas.movierama.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreolas.movierama.base.data.remote.movies.dto.popular.PopularRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.search.SearchRequestApi
import com.andreolas.movierama.home.domain.model.PopularMovie
import com.andreolas.movierama.home.domain.usecase.GetPopularMoviesUseCase
import com.andreolas.movierama.home.domain.usecase.GetSearchMoviesUseCase
import com.andreolas.movierama.home.domain.usecase.MarkAsFavoriteUseCase
import com.andreolas.movierama.ui.UIText
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getSearchMoviesUseCase: GetSearchMoviesUseCase,
    private val markAsFavoriteUseCase: MarkAsFavoriteUseCase,
) : ViewModel() {
    private var currentPage: Int = 1
    private var searchPage: Int = 1

    private var searchJob: Job? = null
    private var allowSearchResult: Boolean = true

    private var latestQuery: String? = null
    private var cachedSearchResults: HashMap<String, SearchCache> = hashMapOf()

    private val _viewState: MutableStateFlow<HomeViewState> = MutableStateFlow(
        HomeViewState(
            isLoading = true,
            moviesList = emptyList(),
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
            getPopularMoviesUseCase.invoke(
                parameters = PopularRequestApi(
                    page = currentPage,
                )
            ).collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        _viewState.update { viewState ->

                            val updatedList = getUpdatedMovies(
                                currentMoviesList = viewState.moviesList,
                                updatedMoviesList = result.data,
                            )

                            viewState.copy(
                                isLoading = false,
                                moviesList = updatedList,
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
        _viewState.update { viewState ->
            viewState.copy(
                selectedMovie = movie,
            )
        }
    }

    fun onMarkAsFavoriteClicked(movie: PopularMovie) {
        viewModelScope.launch {
            markAsFavoriteUseCase(movie)
        }
    }

    /**
     * Checks whether to load more popular movies,
     * or make a search query with incremented page.
     */
    fun onLoadNextPage() {
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
                    searchLoading = true,
                )
            }
            searchJob = viewModelScope.launch {
                delay(timeMillis = 300)
                if (cachedSearchResults.contains(query) && searchPage == 1) {
                    Timber.d("Fetching cached results")
                    _viewState.update { viewState ->
                        latestQuery = query
                        viewState.copy(
                            searchLoading = false,
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
                searchLoading = false,
                query = "",
                emptyResult = false,
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
                                    searchLoading = true,
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
                                        searchLoading = false,
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
                                    searchLoading = false,
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
}

data class SearchCache(
    var page: Int,
    var result: MutableList<PopularMovie>,
)
