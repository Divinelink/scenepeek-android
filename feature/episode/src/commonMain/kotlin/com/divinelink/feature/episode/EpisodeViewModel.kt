package com.divinelink.feature.episode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.commons.data
import com.divinelink.core.commons.provider.ClockProvider
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.domain.track.MarkEpisodeAsWatchedUseCase
import com.divinelink.core.domain.track.MarkEpisodeParameters
import com.divinelink.core.model.tab.EpisodeTab
import com.divinelink.core.navigation.route.Navigation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EpisodeViewModel(
  route: Navigation.Episode,
  private val repository: MediaRepository,
  private val markEpisodeAsWatchedUseCase: MarkEpisodeAsWatchedUseCase,
  private val clock: ClockProvider,
) : ViewModel() {

  private val _uiState: MutableStateFlow<EpisodeUiState> = MutableStateFlow(
    EpisodeUiState.initial(route),
  )
  val uiState: StateFlow<EpisodeUiState> = _uiState

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

    repository.getWatchedEpisodesForSeason(
      showId = route.showId,
      seasonNumber = route.seasonNumber,
    )
      .distinctUntilChanged()
      .onEach { episodes ->
        _uiState.update { it.copy(watchedEpisodes = episodes) }
      }
      .launchIn(viewModelScope)
  }

  private fun fetchEpisode() {
    val currentIndex = uiState.value.selectedIndex
    val currentEpisode = uiState.value.episode?.number ?: 1
    val index = if (currentIndex == -1) {
      uiState.value.tabs.indexOf(EpisodeTab(currentEpisode)).takeIf { it != -1 } ?: 0
    } else {
      currentIndex
    }
    _uiState.update { it.copy(selectedIndex = index) }

    val cachedEpisode = uiState.value.episodes[index]

    if (cachedEpisode == null) {
      repository.fetchEpisode(
        showId = uiState.value.showId,
        season = uiState.value.seasonNumber,
        number = uiState.value.tabs[index].number,
      )
        .distinctUntilChanged()
        .onEach {
          _uiState.update { uiState ->
            val episode = it.data

            uiState.copy(
              episode = episode,
              episodes = uiState.episodes.plus(index to episode),
            )
          }
        }
        .launchIn(viewModelScope)
    } else {
      _uiState.update { uiState ->
        uiState.copy(episode = cachedEpisode)
      }
    }
  }

  fun onAction(action: EpisodeAction) {
    when (action) {
      is EpisodeAction.OnSelectEpisode -> handleOnSelectEpisode(action)
      EpisodeAction.DismissSnackbar -> handleDismissSnackbar()
      is EpisodeAction.MarkAsWatched -> handleMarkEpisodeAsWatched()
      is EpisodeAction.SetRatingState -> handleSetRatingState(action)
    }
  }

  private fun handleOnSelectEpisode(action: EpisodeAction.OnSelectEpisode) {
    if (action.index != -1) {
      _uiState.update { uiState ->
        uiState.copy(selectedIndex = action.index)
      }
    }

    fetchEpisode()
  }

  private fun handleSetRatingState(action: EpisodeAction.SetRatingState) {
    _uiState.update {
      it.copy(
        ratingLoading = action.loading,
      )
    }
  }

  private fun handleDismissSnackbar() {
    _uiState.update { uiState ->
      uiState.copy(snackbarMessage = null)
    }
  }

  private fun handleMarkEpisodeAsWatched() {
    val episode = uiState.value.episode ?: return

    viewModelScope.launch {
      markEpisodeAsWatchedUseCase(
        parameters = MarkEpisodeParameters(
          episode = episode,
          watchedAt = clock.epochSeconds,
        ),
      )
    }
  }
}
