package com.divinelink.feature.home.nextup

import androidx.compose.material3.SnackbarResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.commons.provider.ClockProvider
import com.divinelink.core.data.auth.AuthRepository
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.data.tabs.TabsRepository
import com.divinelink.core.data.track.TrackRepository
import com.divinelink.core.domain.track.MarkEpisodeAsWatchedUseCase
import com.divinelink.core.domain.track.MarkEpisodeParameters
import com.divinelink.core.model.UIText
import com.divinelink.core.model.details.Episode
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.tab.ProgressTab
import com.divinelink.core.network.Resource
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.resources.core_ui_add_rating
import com.divinelink.core.ui.resources.saved_to_history
import com.divinelink.core.ui.snackbar.SnackbarMessage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NextUpViewModel(
  authRepository: AuthRepository,
  private val repository: MediaRepository,
  private val trackRepository: TrackRepository,
  private val tabsRepository: TabsRepository,
  private val markEpisodeAsWatchedUseCase: MarkEpisodeAsWatchedUseCase,
  private val clock: ClockProvider,
) : ViewModel() {

  private val _uiState: MutableStateFlow<NextUpUiState> = MutableStateFlow(
    NextUpUiState.initial,
  )
  val uiState: StateFlow<NextUpUiState> = _uiState

  private val _rateEpisodeModal = Channel<Episode>()
  val rateEpisodeModal: Flow<Episode> = _rateEpisodeModal.receiveAsFlow()

  init {
    combine(
      repository.getNextUpEpisodes(),
      trackRepository.getSyncErrors(),
    ) { episodes, errors ->
      val newTabs = tabsRepository.nextUpTabs.value
        .toggle(ProgressTab.OnHold, episodes.paused.isNotEmpty())
        .toggle(ProgressTab.Upcoming, episodes.upcoming.isNotEmpty())
      tabsRepository.setNextUpTabs(newTabs)

      _uiState.update { state ->
        state.copy(
          available = episodes.available,
          upcoming = episodes.upcoming,
          paused = episodes.paused,
          errors = errors,
          loading = false,
        )
      }
    }
      .launchIn(viewModelScope)

    authRepository
      .tmdbAccount
      .onEach { account ->
        _uiState.update { it.copy(loggedIn = account != null) }
      }
      .launchIn(viewModelScope)
  }

  private fun onRetry() {
    val currentErrors = _uiState.value.errors
    if (currentErrors.isEmpty()) return

    _uiState.update { it.copy(loading = true) }

    viewModelScope.launch {
      val syncedShows = mutableListOf<Long>()

      currentErrors.forEach { syncError ->
        val result = repository.fetchSeasonDetails(
          showId = syncError.showId,
          season = syncError.targetSeason.toInt(),
          forceRefresh = false,
        ).first { it !is Resource.Loading }

        when (result) {
          is Resource.Error -> if (
            result.error is AppException.NotFound ||
            result.error is AppException.BadRequest
          ) {
            // This only happens when the user is trying to fetch a season that does not exist
            // - meaning the show is completed
            syncedShows.add(syncError.showId)
          }
          is Resource.Success -> syncedShows.add(syncError.showId)
          is Resource.Loading -> Unit
        }
      }

      if (syncedShows.isNotEmpty()) {
        trackRepository.clearSyncErrors(syncedShows)
      }

      _uiState.update { it.copy(loading = false) }
    }
  }

  private fun markEpisodeAsWatched(
    episode: Episode,
    watchedAt: Long,
  ) {
    viewModelScope.launch {
      markEpisodeAsWatchedUseCase(
        MarkEpisodeParameters(
          episode = episode,
          watchedAt = watchedAt,
        ),
      ).onSuccess {
        setSnackbarMessage(
          SnackbarMessage.from(
            text = UIText.ResourceText(UiString.saved_to_history, episode.name),
            actionLabelText = if (_uiState.value.loggedIn) {
              UIText.ResourceText(UiString.core_ui_add_rating)
            } else {
              null
            },
            onSnackbarResult = { result ->
              when (result) {
                SnackbarResult.Dismissed -> Unit
                SnackbarResult.ActionPerformed -> viewModelScope.launch {
                  _rateEpisodeModal.send(episode)
                }
              }
            },
          ),
        )
      }
    }
  }

  private fun setSnackbarMessage(snackbarMessage: SnackbarMessage?) {
    _uiState.update { it.copy(snackbarMessage = snackbarMessage) }
  }

  fun onAction(action: NextUpAction) {
    when (action) {
      NextUpAction.Retry -> onRetry()
      NextUpAction.DismissSnackbarMessage -> setSnackbarMessage(null)
      is NextUpAction.MarkEpisodeAsWatched -> markEpisodeAsWatched(
        episode = action.episode,
        watchedAt = clock.epochSeconds,
      )
    }
  }

  private fun Set<ProgressTab>.toggle(
    tab: ProgressTab,
    present: Boolean,
  ): Set<ProgressTab> = if (present) this + tab else this - tab
}
