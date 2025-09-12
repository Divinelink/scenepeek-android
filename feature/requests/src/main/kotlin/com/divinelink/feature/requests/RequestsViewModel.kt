package com.divinelink.feature.requests

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RequestsViewModel : ViewModel() {

  private val _uiState: MutableStateFlow<RequestsUiState> =
    MutableStateFlow(RequestsUiState.initial)
  val uiState: StateFlow<RequestsUiState> = _uiState
}
