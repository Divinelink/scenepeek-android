package com.divinelink.feature.updater

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.app.AppInfoRepository
import com.divinelink.core.model.LCEState
import com.divinelink.core.network.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class UpdaterViewModel(
  appInfoRepository: AppInfoRepository,
) : ViewModel() {

  private val _uiState: MutableStateFlow<UpdaterUiState> = MutableStateFlow(UpdaterUiState.initial)
  val uiState: StateFlow<UpdaterUiState> = _uiState

  init {
    appInfoRepository
      .fetchLatestAppVersion(fetchRemote = false)
      .distinctUntilChanged()
      .onEach { result ->
        _uiState.update { state ->
          when (result) {
            is Resource.Error -> state.copy(appVersion = LCEState.Error)
            is Resource.Loading -> state.copy(appVersion = LCEState.Loading)
            is Resource.Success -> state.copy(appVersion = LCEState.Content(result.data))
          }
        }
      }
      .launchIn(viewModelScope)
  }
}
