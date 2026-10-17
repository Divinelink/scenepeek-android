package com.divinelink.feature.add.to.account.rate.ui

import androidx.annotation.VisibleForTesting
import androidx.compose.material3.SnackbarResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.account.AccountRepository
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.domain.components.submit.SubmitRatingAction
import com.divinelink.core.domain.details.media.DeleteRatingParameters
import com.divinelink.core.domain.details.media.DeleteRatingUseCase
import com.divinelink.core.domain.details.media.SubmitRatingParameters
import com.divinelink.core.domain.details.media.SubmitRatingUseCase
import com.divinelink.core.model.UIText
import com.divinelink.core.model.exception.SessionException
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.resources.core_ui_error_retry
import com.divinelink.core.ui.resources.core_ui_login
import com.divinelink.core.ui.snackbar.SnackbarMessage
import com.divinelink.feature.add.to.account.resources.Res
import com.divinelink.feature.add.to.account.resources.must_be_logged_in_to_rate
import com.divinelink.feature.add.to.account.resources.rating_deleted_successfully
import com.divinelink.feature.add.to.account.resources.rating_submitted_successfully
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SubmitRatingViewModel(
  route: Navigation.SubmitRatingRoute,
  private val repository: AccountRepository,
  private val mediaRepository: MediaRepository,
  private val submitRatingUseCase: SubmitRatingUseCase,
  private val deleteRatingUseCase: DeleteRatingUseCase,
) : ViewModel() {

  private val _uiState: MutableStateFlow<SubmitRatingUiState> = MutableStateFlow(
    SubmitRatingUiState.initial(route),
  )
  val uiState: StateFlow<SubmitRatingUiState> = _uiState

  private val _navigateToLogin = Channel<Unit>()
  val navigateToLogin: Flow<Unit> = _navigateToLogin.receiveAsFlow()

  fun init(route: Navigation.SubmitRatingRoute) {
    _uiState.update {
      it.copy(
        route = route,
        value = route.value,
        title = route.title,
      )
    }
  }

  fun action(action: SubmitRatingAction) {
    when (action) {
      SubmitRatingAction.ClearRating -> handleClearRating()
      is SubmitRatingAction.SubmitRating -> handleSubmitRating(action)
      SubmitRatingAction.DismissSnackbar -> _uiState.update { it.copy(snackbarMessage = null) }
    }
  }

  private fun handleClearRating() {
    when (val route = _uiState.value.route) {
      is Navigation.SubmitRatingRoute.Episode -> handleClearEpisode(route)
      is Navigation.SubmitRatingRoute.Media -> handleClearMediaRating(route)
    }
  }

  private fun handleClearEpisode(route: Navigation.SubmitRatingRoute.Episode) {
    viewModelScope.launch {
      repository.deleteEpisodeRating(
        showId = route.mediaId,
        season = route.seasonNumber,
        number = route.episodeNumber,
      ).fold(
        onSuccess = {
          _uiState.update { uiState ->
            uiState.copy(
              snackbarMessage = SnackbarMessage.from(
                text = UIText.ResourceText(
                  Res.string.rating_deleted_successfully,
                  uiState.title,
                ),
              ),
            )
          }
        },
        onFailure = {
          _uiState.update { uiState ->
            uiState.copy(
              snackbarMessage = SnackbarMessage.from(
                text = UIText.ResourceText(UiString.core_ui_error_retry),
              ),
            )
          }
        },
      )
    }
  }

  private fun handleClearMediaRating(route: Navigation.SubmitRatingRoute.Media) {
    viewModelScope.launch {
      deleteRatingUseCase.invoke(
        DeleteRatingParameters(
          id = route.mediaId,
          mediaType = route.type,
        ),
      ).fold(
        onSuccess = {
          _uiState.update { state ->
            state.copy(
              snackbarMessage = SnackbarMessage.from(
                text = UIText.ResourceText(Res.string.rating_deleted_successfully, state.title),
              ),
            )
          }
        },
        onFailure = { handleSubmitError(it) },
      )
    }
  }

  private fun handleSubmitRating(action: SubmitRatingAction.SubmitRating) {
    viewModelScope.launch {
      when (val route = _uiState.value.route) {
        is Navigation.SubmitRatingRoute.Episode -> handleSubmitEpisodeRating(
          route = route,
          rating = action.rating.toInt(),
        )
        is Navigation.SubmitRatingRoute.Media -> submitRatingUseCase(
          SubmitRatingParameters(
            id = route.mediaId,
            mediaType = route.type,
            rating = action.rating.toInt(),
          ),
        ).fold(
          onSuccess = { handleSubmitSuccess(route.title) },
          onFailure = { error -> handleSubmitError(error) },
        )
      }
    }
  }

  @VisibleForTesting
  fun navigateToLogin(snackbarResult: SnackbarResult) {
    if (snackbarResult == SnackbarResult.ActionPerformed) {
      _uiState.update { viewState ->
        viewState.copy(
          snackbarMessage = null,
        )
      }

      viewModelScope.launch {
        _navigateToLogin.send(Unit)
      }
    }
  }

  suspend fun handleSubmitEpisodeRating(
    route: Navigation.SubmitRatingRoute.Episode,
    rating: Int,
  ) {
    repository.submitEpisodeRating(
      showId = route.mediaId,
      season = route.seasonNumber,
      number = route.episodeNumber,
      rating = rating,
    ).fold(
      onSuccess = {
        handleSubmitSuccess(route.title)

        mediaRepository.insertEpisodeRating(
          showId = route.mediaId,
          season = route.seasonNumber,
          number = route.episodeNumber,
          rating = rating,
        )
      },
      onFailure = { error ->
        handleSubmitError(error)
      },
    )
  }

  private fun handleSubmitSuccess(title: String) {
    _uiState.update { state ->
      state.copy(
        snackbarMessage = SnackbarMessage.from(
          text = UIText.ResourceText(Res.string.rating_submitted_successfully, title),
        ),
      )
    }
  }

  private fun handleSubmitError(error: Throwable) {
    val snackbarMessage = if (error is SessionException.Unauthenticated) {
      SnackbarMessage.from(
        text = UIText.ResourceText(Res.string.must_be_logged_in_to_rate),
        actionLabelText = UIText.ResourceText(UiString.core_ui_login),
        onSnackbarResult = ::navigateToLogin,
      )
    } else {
      SnackbarMessage.from(text = UIText.ResourceText(UiString.core_ui_error_retry))
    }
    _uiState.update { state ->
      state.copy(
        snackbarMessage = snackbarMessage,
      )
    }
  }
}
