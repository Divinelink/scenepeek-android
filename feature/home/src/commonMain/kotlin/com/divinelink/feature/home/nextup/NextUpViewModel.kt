package com.divinelink.feature.home.nextup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.commons.provider.ClockProvider
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.data.track.TrackRepository
import com.divinelink.core.domain.track.MarkEpisodeAsWatchedUseCase
import com.divinelink.core.domain.track.MarkEpisodeParameters
import com.divinelink.core.model.details.Episode
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.network.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class NextUpViewModel(
  private val repository: MediaRepository,
  private val trackRepository: TrackRepository,
  private val markEpisodeAsWatchedUseCase: MarkEpisodeAsWatchedUseCase,
  private val clock: ClockProvider,
) : ViewModel() {

  private val _uiState: MutableStateFlow<NextUpUiState> = MutableStateFlow(
    NextUpUiState.initial,
  )
  val uiState: StateFlow<NextUpUiState> = _uiState

  init {
    combine(
      repository.getNextUpEpisodes(),
      trackRepository.getSyncErrors(),
    ) { episodes, errors ->
      _uiState.update { state ->
        state.copy(
          available = episodes.available,
          upcoming = episodes.upcoming,
          errors = errors,
          loading = false,
        )
      }
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
    watchedAt: LocalDate,
  ) {
    viewModelScope.launch {
      markEpisodeAsWatchedUseCase(
        MarkEpisodeParameters(
          episode = episode,
          watchedAt = watchedAt,
        ),
      )
    }
  }

  fun onAction(action: NextUpAction) {
    when (action) {
      NextUpAction.Retry -> onRetry()
      is NextUpAction.MarkEpisodeAsWatched -> markEpisodeAsWatched(
        episode = action.episode,
        watchedAt = clock.now,
      )
    }
  }
}
