package com.andreolas.movierama.details.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreolas.movierama.R
import com.andreolas.movierama.base.data.remote.movies.dto.details.DetailsRequestApi
import com.andreolas.movierama.destinations.DetailsScreenDestination
import com.andreolas.movierama.details.domain.model.MovieDetailsException
import com.andreolas.movierama.details.domain.model.MovieDetailsResult
import com.andreolas.movierama.details.domain.usecase.AccountMediaDetailsParams
import com.andreolas.movierama.details.domain.usecase.FetchAccountMediaDetailsUseCase
import com.andreolas.movierama.details.domain.usecase.GetMovieDetailsUseCase
import com.andreolas.movierama.details.domain.usecase.SubmitRatingParameters
import com.andreolas.movierama.details.domain.usecase.SubmitRatingUseCase
import com.andreolas.movierama.home.domain.model.MediaType
import com.andreolas.movierama.home.domain.usecase.MarkAsFavoriteUseCase
import com.andreolas.movierama.ui.UIText
import com.andreolas.movierama.ui.components.snackbar.SnackbarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.divinelink.core.util.domain.data
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
  getMovieDetailsUseCase: GetMovieDetailsUseCase,
  private val onMarkAsFavoriteUseCase: MarkAsFavoriteUseCase,
  private val fetchAccountMediaDetailsUseCase: FetchAccountMediaDetailsUseCase,
  private val submitRatingUseCase: SubmitRatingUseCase,
  savedStateHandle: SavedStateHandle,
) : ViewModel() {

  private val args: DetailsNavArguments = DetailsScreenDestination.argsFrom(savedStateHandle)

  private val _viewState: MutableStateFlow<DetailsViewState> = MutableStateFlow(
    value = DetailsViewState(
      movieId = args.id,
      mediaType = MediaType.from(args.mediaType),
      isLoading = true,
    )
  )
  val viewState: StateFlow<DetailsViewState> = _viewState.asStateFlow()

  fun onMarkAsFavorite() {
    viewModelScope.launch {
      viewState.value.mediaItem?.let { mediaItem ->
        onMarkAsFavoriteUseCase(mediaItem).onSuccess {
          _viewState.update { viewState ->
            viewState.copy(
              mediaDetails = viewState.mediaDetails?.copy(
                isFavorite = !viewState.mediaDetails.isFavorite
              ),
            )
          }
        }
      }
    }
  }

  init {
    val requestApi = when (viewState.value.mediaType) {
      MediaType.TV -> DetailsRequestApi.TV(args.id)
      MediaType.MOVIE -> DetailsRequestApi.Movie(args.id)
      else -> throw IllegalArgumentException("Unknown media value")
    }
    getMovieDetailsUseCase(
      parameters = requestApi,
    ).onEach { result ->
      result.onSuccess {
        _viewState.update { viewState ->
          when (result.data) {
            is MovieDetailsResult.DetailsSuccess -> {
              viewState.copy(
                isLoading = false,
                mediaDetails = (result.data as MovieDetailsResult.DetailsSuccess).mediaDetails
              )
            }

            is MovieDetailsResult.ReviewsSuccess -> viewState.copy(
              reviews = (result.data as MovieDetailsResult.ReviewsSuccess).reviews,
            )

            is MovieDetailsResult.SimilarSuccess -> viewState.copy(
              similarMovies = (result.data as MovieDetailsResult.SimilarSuccess).similar,
            )

            is MovieDetailsResult.VideosSuccess -> viewState.copy(
              trailer = (result.data as MovieDetailsResult.VideosSuccess).trailer,
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
      }.onFailure {
        if (it is MovieDetailsException) {
          _viewState.update { viewState ->
            viewState.copy(
              error = MovieDetailsResult.Failure.FatalError().message,
              isLoading = false,
            )
          }
        } else {
          _viewState.update { viewState ->
            viewState.copy(
              error = MovieDetailsResult.Failure.Unknown.message,
              isLoading = false,
            )
          }
        }
      }
    }.onCompletion {
      fetchAccountMediaDetails()
    }.launchIn(viewModelScope)
  }

  fun onSubmitRate(rating: Int) {
    viewModelScope.launch {
      submitRatingUseCase.invoke(
        SubmitRatingParameters(
          id = viewState.value.movieId,
          mediaType = viewState.value.mediaType,
          rating = rating
        )
      ).collectLatest { result ->
        result.onSuccess {
          Timber.d(
            "Rating submitted: $rating"
          )
          _viewState.update { viewState ->
            viewState.copy(
              userRating = rating.toString(),
              snackbarMessage = SnackbarMessage.from(
                text = UIText.ResourceText(
                  R.string.details__rating_submitted_successfully,
                  viewState.mediaDetails?.title ?: ""
                ),
              )
            )
          }
        }
      }
    }
  }

  fun consumeSnackbarMessage() {
    _viewState.update { viewState ->
      viewState.copy(
        snackbarMessage = null
      )
    }
  }

  private suspend fun fetchAccountMediaDetails() {
    val params = AccountMediaDetailsParams(
      id = viewState.value.movieId,
      mediaType = viewState.value.mediaType
    )

    fetchAccountMediaDetailsUseCase.invoke(params)
      .collectLatest { result ->
        result.onSuccess {
          _viewState.update { viewState ->
            viewState.copy(
              userRating = result.data.rating?.toInt()?.toString(),
            )
          }
        }
      }
  }
}
