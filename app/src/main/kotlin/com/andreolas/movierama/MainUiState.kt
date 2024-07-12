package com.andreolas.movierama

sealed interface MainUiState {

  data object Loading : MainUiState

  data object Completed : MainUiState
}
