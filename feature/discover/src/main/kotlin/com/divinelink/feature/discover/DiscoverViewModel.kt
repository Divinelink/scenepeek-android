package com.divinelink.feature.discover

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DiscoverViewModel : ViewModel() {

  private val _uiState: MutableStateFlow<DiscoverUiState> =
    MutableStateFlow(DiscoverUiState.initial)
  val uiState: StateFlow<DiscoverUiState> = _uiState
}
