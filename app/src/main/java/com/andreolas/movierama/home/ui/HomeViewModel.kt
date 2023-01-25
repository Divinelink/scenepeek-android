package com.andreolas.movierama.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreolas.movierama.base.data.remote.movies.dto.popular.PopularRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.search.SearchRequestApi
import com.andreolas.movierama.home.domain.model.PopularMovie
import com.andreolas.movierama.home.domain.usecase.GetPopularMoviesUseCase
import com.andreolas.movierama.home.domain.usecase.GetSearchMoviesUseCase
import com.andreolas.movierama.home.domain.usecase.MarkAsFavoriteUseCase
import com.andreolas.movierama.home.domain.usecase.RemoveFavoriteUseCase
import com.andreolas.movierama.ui.UIText
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.divinelink.core.util.domain.Result
import gr.divinelink.core.util.domain.succeeded
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
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
) : ViewModel() {
    private var currentPage: Int = 1
    private var searchPage: Int = 1

    private var searchJob: Job? = null
    private var allowSearchResult: Boolean = true

    private var cachedMovies: MutableList<PopularMovie> = mutableListOf()

    private val _viewState: MutableStateFlow<HomeViewState> = MutableStateFlow(
        HomeViewState(
            isLoading = true,
            moviesList = cachedMovies,
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
                            cachedMovies = (viewState.moviesList + result.data)
                                .distinctBy { it.id }
                                .toMutableList()
                            viewState.copy(
                                isLoading = false,
                                moviesList = cachedMovies,
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

    @Suppress("UnusedPrivateMember")
    fun onMovieClicked(movie: PopularMovie) {
        _viewState.update { viewState ->
            viewState.copy(
                selectedMovie = movie,
            )
        }
    }

    fun onBottomSheetClosed() {
        _viewState.update { viewState ->
            viewState.copy(
                selectedMovie = null,
            )
        }
    }

    fun onMarkAsFavoriteClicked(movie: PopularMovie) {
        viewModelScope.launch {
            val result = if (movie.isFavorite) {
                removeFavoriteUseCase(movie.id)
            } else {
                markAsFavoriteUseCase(movie)
            }
            if (result.succeeded) {
                // todo Add Snackbar when movie is added or removed.
                updateFavoriteStatus(movie)
            }
        }
    }

    private fun updateFavoriteStatus(movie: PopularMovie) {
        _viewState.update { viewState ->
            viewState.copy(
                moviesList = viewState.moviesList.map { currentMovie ->
                    if (currentMovie.id == movie.id) {
                        Timber.d("Movie ${movie.title} favorite property changed.")
                        currentMovie.copy(isFavorite = !movie.isFavorite)
                    } else {
                        currentMovie
                    }
                },
                selectedMovie = if (viewState.selectedMovie?.id == movie.id) {
                    movie.copy(isFavorite = !movie.isFavorite)
                } else {
                    viewState.selectedMovie
                },
            )
        }.also {
            // This is needed to keep the Favorite status on Popular Movies.
            if (viewState.value.query.isEmpty()) {
                cachedMovies = viewState.value.moviesList.toMutableList()
            }
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
                fetchFromSearchQuery(query = query, page = 1)
            }
        }
    }

    fun onClearClicked() {
        searchJob?.cancel()
        searchPage = 1
        allowSearchResult = false
        _viewState.update { viewState ->
            viewState.copy(
                moviesList = cachedMovies,
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
                                val movies = accumulateSearchMovies(page, result)
                                viewState.copy(
                                    searchLoading = false,
                                    moviesList = movies,
                                    emptyResult = movies.isEmpty(),
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

    private fun accumulateSearchMovies(
        page: Int,
        result: Result.Success<List<PopularMovie>>,
    ): List<PopularMovie> {
        val movies = if (page == 1) {
            result.data
        } else {
            (_viewState.value.moviesList + result.data).distinctBy { it.id }
        }
        return movies
    }
}
