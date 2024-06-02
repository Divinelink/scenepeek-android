package com.andreolas.movierama.details.ui

import androidx.compose.material3.SnackbarResult
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreolas.movierama.R
import com.andreolas.movierama.destinations.DetailsScreenDestination
import com.andreolas.movierama.details.domain.usecase.AccountMediaDetailsParams
import com.andreolas.movierama.details.domain.usecase.AddToWatchlistParameters
import com.andreolas.movierama.details.domain.usecase.AddToWatchlistUseCase
import com.andreolas.movierama.details.domain.usecase.DeleteRatingParameters
import com.andreolas.movierama.details.domain.usecase.DeleteRatingUseCase
import com.andreolas.movierama.details.domain.usecase.FetchAccountMediaDetailsUseCase
import com.andreolas.movierama.details.domain.usecase.GetMovieDetailsUseCase
import com.andreolas.movierama.details.domain.usecase.SubmitRatingParameters
import com.andreolas.movierama.details.domain.usecase.SubmitRatingUseCase
import com.andreolas.movierama.home.domain.usecase.MarkAsFavoriteUseCase
import com.andreolas.movierama.ui.UIText
import com.andreolas.movierama.ui.components.snackbar.SnackbarMessage
import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.details.model.MediaDetailsException
import com.divinelink.core.data.session.model.SessionException
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.media.model.details.DetailsRequestApi
import dagger.hilt.android.lifecycle.HiltViewModel
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
  private val deleteRatingUseCase: DeleteRatingUseCase,
  private val addToWatchlistUseCase: AddToWatchlistUseCase,
  savedStateHandle: SavedStateHandle,
) : ViewModel() {

  private val args: DetailsNavArguments = DetailsScreenDestination.argsFrom(savedStateHandle)

  private val _viewState: MutableStateFlow<DetailsViewState> = MutableStateFlow(
    value = DetailsViewState(
      mediaId = args.id,
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
        if (it is MediaDetailsException) {
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
          id = viewState.value.mediaId,
          mediaType = viewState.value.mediaType,
          rating = rating
        )
      ).collectLatest { result ->
        result.onSuccess {
          Timber.d("Rating submitted: $rating")
          _viewState.update { viewState ->
            viewState.copy(
              showRateDialog = false,
              userDetails = viewState.userDetails?.copy(
                rating = rating.toFloat(),
                watchlist = false
              ),
              snackbarMessage = SnackbarMessage.from(
                text = UIText.ResourceText(
                  R.string.details__rating_submitted_successfully,
                  viewState.mediaDetails?.title ?: ""
                ),
              )
            )
          }
        }.onFailure {
          if (it is SessionException.NoSession) {
            _viewState.update { viewState ->
              viewState.copy(
                showRateDialog = false,
                snackbarMessage = SnackbarMessage.from(
                  text = UIText.ResourceText(R.string.details__must_be_logged_in_to_rate),
                  actionLabelText = UIText.ResourceText(R.string.login),
                  onSnackbarResult = ::navigateToLogin,
                )
              )
            }
          }
        }
      }
    }
  }

  fun onClearRating() {
    if (viewState.value.userDetails == null) return

    viewModelScope.launch {
      deleteRatingUseCase.invoke(
        DeleteRatingParameters(
          id = viewState.value.mediaId,
          mediaType = viewState.value.mediaType
        )
      ).collectLatest { result ->
        result.onSuccess {
          Timber.d("Rating deleted")
          _viewState.update { viewState ->
            viewState.copy(
              userDetails = viewState.userDetails?.copy(
                rating = null
              ),
              snackbarMessage = SnackbarMessage.from(
                text = UIText.ResourceText(
                  R.string.details__rating_deleted_successfully,
                  viewState.mediaDetails?.title ?: ""
                )
              ),
              showRateDialog = false
            )
          }
        }
      }
    }
  }

  fun onAddRateClicked() {
    _viewState.update { viewState ->
      viewState.copy(
        showRateDialog = true
      )
    }
  }

  fun onAddToWatchlist() {
    viewModelScope.launch {
      addToWatchlistUseCase.invoke(
        AddToWatchlistParameters(
          id = viewState.value.mediaId,
          mediaType = viewState.value.mediaType,
          addToWatchlist = viewState.value.userDetails?.watchlist == false,
        )
      ).collectLatest { result ->
        result.onSuccess {
          _viewState.update { viewState ->
            if (viewState.userDetails?.watchlist == true) {
              viewState.copy(
                userDetails = viewState.userDetails.copy(watchlist = false),
                snackbarMessage = SnackbarMessage.from(
                  text = UIText.ResourceText(
                    R.string.details__removed_from_watchlist,
                    viewState.mediaDetails?.title!!
                  )
                )
              )
            } else {
              viewState.copy(
                userDetails = viewState.userDetails?.copy(watchlist = true),
                snackbarMessage = SnackbarMessage.from(
                  text = UIText.ResourceText(
                    R.string.details__added_to_watchlist,
                    viewState.mediaDetails?.title!!
                  )
                )
              )
            }
          }
        }.onFailure {
          if (it is SessionException.InvalidAccountId) {
            _viewState.update { viewState ->
              viewState.copy(
                snackbarMessage = SnackbarMessage.from(
                  text = UIText.ResourceText(R.string.details__must_be_logged_in_to_watchlist),
                  actionLabelText = UIText.ResourceText(R.string.login),
                  onSnackbarResult = ::navigateToLogin
                )
              )
            }
          } else {
            _viewState.update { viewState ->
              viewState.copy(
                snackbarMessage = SnackbarMessage.from(
                  text = UIText.ResourceText(R.string.error_retry)
                )
              )
            }
          }
        }
      }
    }
  }

  fun onShareClicked(openShareDialog: Boolean) {
    _viewState.update { viewState ->
      viewState.copy(
        openShareDialog = openShareDialog
      )
    }
  }

  internal fun navigateToLogin(snackbarResult: SnackbarResult) {
    if (snackbarResult == SnackbarResult.ActionPerformed) {
      _viewState.update { viewState ->
        viewState.copy(
          navigateToLogin = true,
          snackbarMessage = null
        )
      }
    }
  }

  private suspend fun fetchAccountMediaDetails() {
    val params = AccountMediaDetailsParams(
      id = viewState.value.mediaId,
      mediaType = viewState.value.mediaType
    )

    fetchAccountMediaDetailsUseCase.invoke(params)
      .collectLatest { result ->
        result.onSuccess {
          _viewState.update { viewState ->
            viewState.copy(
              userDetails = result.data
            )
          }
        }
      }
  }

  // Consumers

  fun consumeNavigateToLogin() {
    _viewState.update { viewState ->
      viewState.copy(navigateToLogin = null)
    }
  }

  fun consumeSnackbarMessage() {
    _viewState.update { viewState ->
      viewState.copy(snackbarMessage = null)
    }
  }

  fun onDismissRateDialog() {
    _viewState.update { viewState ->
      viewState.copy(showRateDialog = false)
    }
  }
}
