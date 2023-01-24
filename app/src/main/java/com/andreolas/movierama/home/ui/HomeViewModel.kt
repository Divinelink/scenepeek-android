package com.andreolas.movierama.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreolas.movierama.base.data.remote.movies.dto.popular.PopularRequestApi
import com.andreolas.movierama.home.domain.model.PopularMovie
import com.andreolas.movierama.home.domain.usecase.GetPopularMoviesUseCase
import com.andreolas.movierama.home.domain.usecase.MarkAsFavoriteUseCase
import com.andreolas.movierama.home.domain.usecase.RemoveFavoriteUseCase
import com.andreolas.movierama.ui.UIText
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.divinelink.core.util.domain.Result
import gr.divinelink.core.util.domain.succeeded
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val markAsFavoriteUseCase: MarkAsFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
) : ViewModel() {
    private var currentPage: Int = 1

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
                        cachedMovies = (_viewState.value.moviesList + result.data).distinctBy { it.id }.toMutableList()
                        _viewState.update { viewState ->
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
        // todo
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
        _viewState.getAndUpdate { viewState ->
            viewState.copy(
                moviesList = viewState.moviesList.map { currentMovie ->
                    if (currentMovie.id == movie.id) {
                        Timber.d("Movie ${movie.title} favorite property changed.")
                        currentMovie.copy(isFavorite = !movie.isFavorite)
                    } else {
                        currentMovie
                    }
                }
            )
        }
    }

    fun onLoadNextPage() {
        if (viewState.value.loadMorePopular) {
            currentPage++
            fetchPopularMovies()
        } else {
            // load next page for searching
        }
    }

    fun onSearchMovies(query: String) {
        if (query.isEmpty()) {
            onClearClicked()
        } else {
            _viewState.update { viewState ->
                viewState.copy(
                    query = query,
                    moviesList = emptyList(),
                    loadMorePopular = false,
                )
            }
        }
    }

    fun onClearClicked() {
        _viewState.update { viewState ->
            viewState.copy(
                moviesList = cachedMovies,
                loadMorePopular = true,
                query = "",
            )
        }
    }
}
