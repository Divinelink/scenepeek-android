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
        if (query != latestQuery) {
            _viewState.update {
                it.copy(searchMovies = null)
            }
        }
        latestQuery = query

        viewModelScope.launch {
            getSearchMoviesUseCase.invoke(
                parameters = SearchRequestApi(
                    query = query,
                    page = page,
                )
            ).collectLatest { result ->
                when (result) {
                    Result.Loading -> {
                        _viewState.update { viewState ->
                            viewState.copy(
                                searchLoading = true,
                            )
                        }
                    }
                    is Result.Success -> {
                        if (allowSearchResult) {
                            _viewState.update { viewState ->

                                val list = getUpdatedMovies(
                                    currentMoviesList = viewState.searchMovies ?: emptyList(),
                                    updatedMoviesList = result.data.combinedList,
                                )

                                Timber.d(
                                    "Cached list for $query contains:  ${cachedSearchResults[query]?.result?.size} items." +
                                    " \n ${cachedSearchResults[query]?.result}"
                                )

                                viewState.copy(
                                    searchLoading = false,
                                    searchMovies = list, // cachedSearchResults[query]?.result ?: emptyList(),
                                    emptyResult = list.isEmpty() // cachedSearchResults[query]?.result?.isEmpty() == true,
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

    private fun updatedSelectedMovie(
        updatedList: List<PopularMovie>,
        selectedMovie: PopularMovie?,
    ): PopularMovie? {
        return updatedList.find { it.id == selectedMovie?.id }
    }

    private fun getUpdatedMovies(
        currentMoviesList: List<PopularMovie>?,
        updatedMoviesList: List<PopularMovie>,
    ): List<PopularMovie> {
        val combinedList = (currentMoviesList?.plus(updatedMoviesList))?.distinctBy { it.id }
        val updatedList = combinedList?.toMutableList()
        updatedMoviesList.forEach { updatedMovie ->
            val index = updatedList?.indexOfFirst { it.id == updatedMovie.id }
            if (index != -1) {
                if (index != null) {
                    updatedList[index] = updatedMovie
                }
            }
        }
        return updatedList?.distinctBy { it.id } ?: emptyList()
    }

    @Suppress("UnusedPrivateMember")
    private fun accumulateSearchMovies(
        page: Int,
        result: List<PopularMovie>,
    ): List<PopularMovie> {
        val movies = if (page == 1) {
            result
        } else {
            _viewState.value.searchMovies
                ?.plus(result)
                ?.distinctBy { it.id }
                ?: emptyList()
        }
        return movies
    }
}

data class SearchCache(
    var page: Int,
    var result: MutableList<PopularMovie>,
)
