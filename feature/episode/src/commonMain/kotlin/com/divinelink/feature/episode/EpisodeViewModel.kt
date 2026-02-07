package com.divinelink.feature.episode

import androidx.annotation.VisibleForTesting
import androidx.compose.material3.SnackbarResult
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.commons.data
import com.divinelink.core.data.account.AccountRepository
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.model.UIText
import com.divinelink.core.model.exception.SessionException
import com.divinelink.core.model.tab.EpisodeTab
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.resources.core_ui_error_retry
import com.divinelink.core.ui.resources.core_ui_login
import com.divinelink.core.ui.snackbar.SnackbarMessage
import com.divinelink.feature.add.to.account.resources.must_be_logged_in_to_rate
import com.divinelink.feature.add.to.account.resources.rating_deleted_successfully
import com.divinelink.feature.add.to.account.resources.rating_submitted_successfully
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.divinelink.feature.add.to.account.resources.Res as AccountRes

class EpisodeViewModel(
  private val repository: MediaRepository,
  private val accountRepository: AccountRepository,
  savedStateHandle: SavedStateHandle,
) : ViewModel() {

  private val route = Navigation.EpisodeRoute(
    showId = savedStateHandle.get<Int>("showId") ?: -1,
    showTitle = savedStateHandle.get<String>("showTitle") ?: "",
    seasonTitle = savedStateHandle.get<String>("seasonTitle") ?: "",
    seasonNumber = savedStateHandle.get<Int>("seasonNumber") ?: -1,
    episodeIndex = savedStateHandle.get<Int>("episodeIndex") ?: -1,
  )

  private val _uiState: MutableStateFlow<EpisodeUiState> = MutableStateFlow(
    EpisodeUiState.initial(route),
  )
  val uiState: StateFlow<EpisodeUiState> = _uiState

  private val _navigateToLogin = Channel<Unit>()
  val navigateToLogin: Flow<Unit> = _navigateToLogin.receiveAsFlow()

  init {
    repository
      .getSeasonEpisodesNumber(
        season = route.seasonNumber,
        showId = route.showId,
      ).fold(
        onSuccess = { episodes ->
          _uiState.update { state ->
            state.copy(
              tabs = episodes.map { episodeNumber -> EpisodeTab(episodeNumber) },
            )
          }

          fetchEpisode()
        },
        onFailure = {
        },
      )
  }

  private fun fetchEpisode() {
    // TODO add check if episode does not exist to fetch it from DB
    repository.fetchEpisode(
      showId = uiState.value.showId,
      season = uiState.value.seasonNumber,
      number = uiState.value.tabs[uiState.value.selectedIndex].number,
    )
      .distinctUntilChanged()
      .onEach {
        _uiState.update { uiState ->
          val episode = it.data

          uiState.copy(
            episodes = uiState.episodes.plus(uiState.selectedIndex to episode),
          )
        }
      }
      .launchIn(viewModelScope)
  }

  fun onAction(action: EpisodeAction) {
    when (action) {
      is EpisodeAction.OnSelectEpisode -> handleOnSelectEpisode(action)
      EpisodeAction.OnClearRate -> handleOnClearRate()
      is EpisodeAction.OnSubmitRate -> handleOnSubmitRate(action)
      EpisodeAction.DismissSnackbar -> handleDismissSnackbar()
    }
  }

  private fun handleOnSelectEpisode(action: EpisodeAction.OnSelectEpisode) {
    _uiState.update { uiState ->
      uiState.copy(selectedIndex = action.index)
    }

    fetchEpisode()
  }

  private fun handleOnClearRate() {
    viewModelScope.launch {
      val showId = uiState.value.showId
      val season = uiState.value.seasonNumber
      val number = uiState.value.episode?.number ?: -1

      _uiState.update { it.copy(ratingLoading = true) }

      accountRepository.deleteEpisodeRating(
        showId = showId,
        season = season,
        number = number,
      ).fold(
        onSuccess = {
          _uiState.update { uiState ->
            uiState.copy(
              snackbarMessage = SnackbarMessage.from(
                text = UIText.ResourceText(
                  AccountRes.string.rating_deleted_successfully,
                  uiState.episode?.name ?: "",
                ),
              ),
              ratingLoading = false,
            )
          }
        },
        onFailure = {
          _uiState.update { uiState ->
            uiState.copy(
              snackbarMessage = SnackbarMessage.from(
                text = UIText.ResourceText(UiString.core_ui_error_retry),
              ),
              ratingLoading = false,
            )
          }
        },
      )
    }
  }

  private fun handleOnSubmitRate(action: EpisodeAction.OnSubmitRate) {
    viewModelScope.launch {
      val showId = uiState.value.showId
      val season = uiState.value.seasonNumber
      val number = uiState.value.episode?.number ?: -1
      val rating = action.rate

      _uiState.update { it.copy(ratingLoading = true) }

      accountRepository.submitEpisodeRating(
        showId = showId,
        season = season,
        number = number,
        rating = rating,
      )
        .fold(
          onSuccess = {
            _uiState.update { uiState ->
              uiState.copy(
                snackbarMessage = SnackbarMessage.from(
                  text = UIText.ResourceText(
                    AccountRes.string.rating_submitted_successfully,
                    uiState.episode?.name ?: "",
                  ),
                ),
                ratingLoading = false,
              )
            }

            repository.insertEpisodeRating(
              showId = showId,
              season = season,
              number = number,
              rating = rating,
            )
          },
          onFailure = { error ->
            val snackbarMessage = if (error is SessionException.Unauthenticated) {
              SnackbarMessage.from(
                text = UIText.ResourceText(AccountRes.string.must_be_logged_in_to_rate),
                actionLabelText = UIText.ResourceText(UiString.core_ui_login),
                onSnackbarResult = ::navigateToLogin,
              )
            } else {
              SnackbarMessage.from(text = UIText.ResourceText(UiString.core_ui_error_retry))
            }

            _uiState.update { uiState ->
              uiState.copy(
                snackbarMessage = snackbarMessage,
                ratingLoading = false,
              )
            }
          },
        )
    }
  }

  private fun handleDismissSnackbar() {
    _uiState.update { uiState ->
      uiState.copy(snackbarMessage = null)
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
}
