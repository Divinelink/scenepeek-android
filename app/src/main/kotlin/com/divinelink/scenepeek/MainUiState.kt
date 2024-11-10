package com.divinelink.scenepeek

sealed interface MainUiState {

  data object Loading : MainUiState

  data object Completed : MainUiState
}
