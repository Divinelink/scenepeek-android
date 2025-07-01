package com.divinelink.feature.lists

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ListsViewModel : ViewModel() {

  private val _uiState: MutableStateFlow<ListsUiState> = MutableStateFlow(ListsUiState.initial)
  val uiState: StateFlow<ListsUiState> = _uiState

  fun onLoadMore() {
  }
}
