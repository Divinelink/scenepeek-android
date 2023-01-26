package com.andreolas.movierama.details.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreolas.movierama.base.data.remote.movies.dto.details.DetailsRequestApi
import com.andreolas.movierama.destinations.DetailsScreenDestination
import com.andreolas.movierama.details.domain.usecase.GetMovieDetailsUseCase
import com.andreolas.movierama.home.domain.usecase.MarkAsFavoriteUseCase
import com.andreolas.movierama.ui.UIText
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
        value = DetailsViewState.Initial(
            args.movie,
        )
    )
    val viewState: StateFlow<DetailsViewState> = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            getMovieDetailsUseCase(
                parameters = DetailsRequestApi(
                    movieId = args.movie.id
                )
            ).collect { result ->
                _viewState.value = when (result) {
                    Result.Loading -> DetailsViewState.Initial(
                        movie = args.movie,
                    )
                    is Result.Error -> DetailsViewState.Error(
                        movie = viewState.value.movie,
                        error = UIText.StringText(result.exception.message ?: "Something went wrong.")
                    )
                    is Result.Success -> DetailsViewState.Completed(
                        movie = viewState.value.movie,
                        movieDetails = result.data,
                    )
                }
            }
        }
    }
}
