package com.andreolas.movierama.ui.components.bottomsheet

sealed class BottomSheetUiState<T> {
  data object Hidden : BottomSheetUiState<Nothing>()
  data class Visible<T>(val data: T) : BottomSheetUiState<T>()
}
