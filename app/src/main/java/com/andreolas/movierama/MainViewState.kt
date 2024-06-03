package com.andreolas.movierama

sealed interface MainViewState {

  data object Loading : MainViewState

  data object Completed : MainViewState
}
