package com.divinelink.feature.requests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.jellyseerr.repository.JellyseerrRepository
import com.divinelink.core.model.DataState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class RequestsViewModel(val repository: JellyseerrRepository) : ViewModel() {

  private val _uiState: MutableStateFlow<RequestsUiState> =
    MutableStateFlow(RequestsUiState.initial)

  val uiState: StateFlow<RequestsUiState> = _uiState

  private val requestsFlow = _uiState
    .map { it.copy(loading = true) }
    .distinctUntilChanged { old, new ->
      old.filter == new.filter && old.page == new.page
    }
    .flatMapLatest { state ->
      repository.getRequests(page = state.page, filter = state.filter)
        .onEach { Timber.d(it.data.toString()) }
        .catch { error ->
          Timber.e(error, "Failed to load requests")
        }
    }

  init {
    viewModelScope.launch {
      requestsFlow.collect { response ->
        response.fold(
          onFailure = {
            // Handle error
          },
          onSuccess = { result ->
            _uiState.update { uiState ->
              uiState.copy(
                data = when (uiState.data) {
                  is DataState.Data -> DataState.Data(
                    uiState.data.pages + (result.pageInfo.page to result.results),
                  )
                  DataState.Initial -> DataState.Data(mapOf(1 to result.results))
                },
                loading = false,
                loadingMore = false,
                error = null,
              )
            }
          },
        )
      }
    }
  }
}
