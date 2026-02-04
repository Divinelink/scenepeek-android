package com.divinelink.feature.episode

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.commons.data
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.model.tab.EpisodeTab
import com.divinelink.core.navigation.route.Navigation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class EpisodeViewModel(
  private val repository: MediaRepository,
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
    }
  }

  private fun handleOnSelectEpisode(action: EpisodeAction.OnSelectEpisode) {
    _uiState.update { uiState ->
      uiState.copy(selectedIndex = action.index)
    }

    fetchEpisode()
  }
}
