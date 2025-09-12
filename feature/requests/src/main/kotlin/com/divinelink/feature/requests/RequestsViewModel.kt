package com.divinelink.feature.requests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.jellyseerr.repository.JellyseerrRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class RequestsViewModel(val repository: JellyseerrRepository) : ViewModel() {

  private val _uiState: MutableStateFlow<RequestsUiState> =
    MutableStateFlow(RequestsUiState.initial)
  val uiState: StateFlow<RequestsUiState> = _uiState

  init {
    viewModelScope.launch {
      repository.getRequests(
        page = uiState.value.page,
        filter = uiState.value.filter,
      ).collect {
        Timber.d(it.data.toString())
      }
    }
  }
}
