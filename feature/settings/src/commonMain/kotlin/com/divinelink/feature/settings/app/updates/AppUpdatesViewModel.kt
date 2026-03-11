package com.divinelink.feature.settings.app.updates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.commons.provider.BuildConfigProvider
import com.divinelink.core.data.app.AppInfoRepository
import com.divinelink.core.model.LCEState
import com.divinelink.core.network.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class AppUpdatesViewModel(
  private val appInfoRepository: AppInfoRepository,
  configProvider: BuildConfigProvider,
) : ViewModel() {

  private val _uiState = MutableStateFlow(AppUpdatesUiState.initial(configProvider.versionName))
  val uiState: StateFlow<AppUpdatesUiState> = _uiState

  init {
    appInfoRepository
      .fetchLatestAppVersion(fetchRemote = false)
      .distinctUntilChanged()
      .onEach { result ->
        _uiState.update {
          when (result) {
            is Resource.Error -> it
            is Resource.Loading -> it.copy(appVersion = result.data)
            is Resource.Success -> it.copy(appVersion = result.data)
          }
        }
      }
      .launchIn(viewModelScope)
  }

  fun checkForUpdates() {
    appInfoRepository
      .fetchLatestAppVersion(
        fetchRemote = true,
        force = true,
      )
      .launchIn(viewModelScope)

  }
}
