package com.divinelink.feature.lists.create.backdrop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.list.ListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SelectBackdropViewModel(
  id: Int,
  repository: ListRepository,
) : ViewModel() {

  private val _uiState: MutableStateFlow<SelectBackdropUiState> = MutableStateFlow(
    SelectBackdropUiState.initial,
  )
  val uiState: StateFlow<SelectBackdropUiState> = _uiState

  init {
    viewModelScope.launch {
      repository
        .fetchListsBackdrops(id)
        .collect { backdrops ->
          _uiState.update { state ->
            state.copy(
              backdrops = backdrops,
            )
          }
        }
    }
  }
}
