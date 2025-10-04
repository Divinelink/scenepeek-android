package com.divinelink.feature.discover

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class DiscoverViewModel : ViewModel() {

  private val _uiState: MutableStateFlow<DiscoverUiState> = MutableStateFlow(
    DiscoverUiState.initial,
  )
  val uiState: StateFlow<DiscoverUiState> = _uiState

  fun onAction(action: DiscoverAction) {
    when (action) {
      is DiscoverAction.OnSelectTab -> handleSelectTab(action)
    }
  }

  private fun handleSelectTab(action: DiscoverAction.OnSelectTab) {
    _uiState.update {
      it.copy(selectedTabIndex = action.index)
    }
  }
}
