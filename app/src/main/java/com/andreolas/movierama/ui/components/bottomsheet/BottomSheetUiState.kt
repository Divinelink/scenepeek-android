package com.andreolas.movierama.ui.components.bottomsheet

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState

sealed class BottomSheetUiState<T> {

  data object Hidden : BottomSheetUiState<Nothing>()

  data class Visible<T>(val data: T) : BottomSheetUiState<T>()
}

@OptIn(ExperimentalMaterial3Api::class)
val SheetState.isHidden: Boolean
  get() = !this.isVisible

@OptIn(ExperimentalMaterial3Api::class)
suspend fun <T> animateBottomSheet(
  uiState: BottomSheetUiState<T>,
  bottomSheetState: SheetState,
) {
  when (uiState) {
    BottomSheetUiState.Hidden -> {
      if (bottomSheetState.isVisible) {
        bottomSheetState.hide()
      }
    }
    is BottomSheetUiState.Visible -> {
      if (bottomSheetState.isHidden) {
        bottomSheetState.expand()
      }
    }
  }
}
