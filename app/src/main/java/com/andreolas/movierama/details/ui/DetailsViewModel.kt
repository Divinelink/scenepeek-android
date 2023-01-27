package com.andreolas.movierama.details.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreolas.movierama.base.data.remote.movies.dto.details.DetailsRequestApi
import com.andreolas.movierama.destinations.DetailsScreenDestination
import com.andreolas.movierama.details.domain.model.MovieDetailsException
import com.andreolas.movierama.details.domain.model.MovieDetailsResult
import com.andreolas.movierama.details.domain.usecase.GetMovieDetailsUseCase
import com.andreolas.movierama.home.domain.usecase.MarkAsFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@Suppress("UnusedPrivateMember")
class DetailsViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val onMarkAsFavoriteUseCase: MarkAsFavoriteUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val args: DetailsNavArguments = DetailsScreenDestination.argsFrom(savedStateHandle)

    private val _viewState: MutableStateFlow<DetailsViewState> = MutableStateFlow(
        value = DetailsViewState(
            movieId = args.movieId,
            isFavorite = args.isFavorite,
            isLoading = true,
        )
    )
    val viewState: StateFlow<DetailsViewState> = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            getMovieDetailsUseCase(
                parameters = DetailsRequestApi(
                    movieId = args.movieId
                )
            ).onEach { result ->
                _viewState.update { viewState ->
                    when (result) {
                        is Result.Success -> {
                            when (result.data) {
                                is MovieDetailsResult.DetailsSuccess -> viewState.copy(
                                    isLoading = false,
                                    movieDetails = (result.data as MovieDetailsResult.DetailsSuccess).movieDetails.copy(isFavorite = args.isFavorite),
                                )
                                is MovieDetailsResult.ReviewsSuccess -> viewState.copy(
                                    reviews = (result.data as MovieDetailsResult.ReviewsSuccess).reviews,
                                )
                                is MovieDetailsResult.SimilarSuccess -> viewState.copy(
                                    similarMovies = (result.data as MovieDetailsResult.SimilarSuccess).similar,
                                )
                                is MovieDetailsResult.Failure.FatalError -> viewState.copy(
                                    error = (result.data as MovieDetailsResult.Failure.FatalError).message,
                                    isLoading = false,
                                )
                                MovieDetailsResult.Failure.Unknown -> viewState.copy(
                                    error = MovieDetailsResult.Failure.Unknown.message,
                                    isLoading = false,
                                )
                            }
                        }
                        is Result.Error -> {
                            if (result.exception is MovieDetailsException) {
                                viewState.copy(
                                    error = MovieDetailsResult.Failure.FatalError().message,
                                    isLoading = false,
                                )
                            } else {
                                viewState.copy(
                                    error = MovieDetailsResult.Failure.Unknown.message,
                                    isLoading = false,
                                )
                            }
                        }
                        Result.Loading -> viewState.copy(
                            isLoading = true,
                        )
                    }
                }
            }.collect()
        }
    }
}
