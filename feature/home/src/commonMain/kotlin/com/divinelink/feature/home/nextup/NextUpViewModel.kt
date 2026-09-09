package com.divinelink.feature.home.nextup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.domain.track.MarkEpisodeAsWatchedUseCase
import com.divinelink.core.model.DataState
import com.divinelink.core.model.details.Episode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NextUpViewModel(
  repository: MediaRepository,
  private val markEpisodeAsWatchedUseCase: MarkEpisodeAsWatchedUseCase,
) : ViewModel() {

  private val _uiState: MutableStateFlow<NextUpUiState> = MutableStateFlow(
    NextUpUiState.initial,
  )
  val uiState: StateFlow<NextUpUiState> = _uiState

  init {
    repository
      .getNextUpEpisodes()
      .distinctUntilChanged()
      .onEach { episodes ->
        _uiState.update { state ->
          state.copy(
            data = DataState.Data(
              pages = mapOf(1 to episodes),
            ),
          )
        }
      }
      .launchIn(viewModelScope)
  }

  fun markEpisodeAsWatched(
    episode: Episode,
  ) {
    viewModelScope.launch {
      markEpisodeAsWatchedUseCase(episode)
    }
  }
}
