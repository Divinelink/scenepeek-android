package com.divinelink.core.ui

sealed interface MainUiState {
  data object Loading : MainUiState
  data object Completed : MainUiState
}
