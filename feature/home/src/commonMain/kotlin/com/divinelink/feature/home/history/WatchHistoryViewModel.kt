package com.divinelink.feature.home.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.track.TrackRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class WatchHistoryViewModel(
  private val trackRepository: TrackRepository,
) : ViewModel() {

  private val _uiState: MutableStateFlow<WatchHistoryUiState> = MutableStateFlow(
    WatchHistoryUiState.initial,
  )
  val uiState: StateFlow<WatchHistoryUiState> = _uiState

  init {
    trackRepository
      .getWatchHistory(uiState.value.offset)
      .distinctUntilChanged()
      .onEach { history ->
        _uiState.update { state ->
          state.copy(
            history = history,
          )
        }
      }
      .launchIn(viewModelScope)
  }

  fun onAction() {

  }
}
