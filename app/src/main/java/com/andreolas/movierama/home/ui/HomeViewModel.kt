package com.andreolas.movierama.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreolas.movierama.base.data.remote.popular.dto.PopularRequestApi
import com.andreolas.movierama.home.domain.model.PopularMovie
import com.andreolas.movierama.home.domain.usecase.GetPopularMoviesUseCase
import com.andreolas.movierama.home.domain.usecase.MarkAsFavoriteUseCase
import com.andreolas.movierama.home.domain.usecase.RemoveFavoriteUseCase
import com.andreolas.movierama.ui.UIText
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val markAsFavoriteUseCase: MarkAsFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
) : ViewModel() {

    private val _viewState: MutableStateFlow<HomeViewState> = MutableStateFlow(
        HomeViewState(
            isLoading = true,
            moviesList = listOf(),
            selectedMovie = null,
            error = null,
        )
    )
    val viewState: StateFlow<HomeViewState> = _viewState

    init {
        viewModelScope.launch {
            getPopularMoviesUseCase.invoke(
                parameters = PopularRequestApi(
                    apiKey = "30842f7c80f80bb3ad8a2fb98195544d", // System.getenv("TMDB_API_KEY")!!,
                    page = 1
                )
            ).collect { result ->
                when (result) {
                    is Result.Success -> {
                        _viewState.update { viewState ->
                            viewState.copy(
                                isLoading = false,
                                moviesList = result.data,
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
        // todo
    }

    fun onMarkAsFavoriteClicked(movie: PopularMovie) {
        // todo
    }
}
