package com.divinelink.feature.awards.popular

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.awards.AwardsRepository
import com.divinelink.core.ui.blankslate.toBlankSlateState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AwardsViewModel(
  private val awardsRepository: AwardsRepository,
) : ViewModel() {

  private val _uiState: MutableStateFlow<AwardsUiState> = MutableStateFlow(
    AwardsUiState.initial,
  )
  val uiState: StateFlow<AwardsUiState> = _uiState

  init {
    fetchCeremonies()
  }

  fun onAction(action: AwardsAction) {
    when (action) {
      is AwardsAction.OnCeremonyClick -> Unit
      AwardsAction.OnRetry -> fetchCeremonies()
    }
  }

  private fun fetchCeremonies() {
    viewModelScope.launch {
      _uiState.update { state ->
        state.copy(
          loading = true,
          error = null,
        )
      }

      awardsRepository
        .fetchAwardCeremonies()
        .fold(
          onSuccess = { result ->
            _uiState.update { state ->
              state.copy(
                loading = false,
                error = null,
                ceremonies = result,
              )
            }
          },
          onFailure = { error ->
            _uiState.update { state ->
              state.copy(
                loading = false,
                error = error.toBlankSlateState(),
              )
            }
          },
        )
    }
  }
}
