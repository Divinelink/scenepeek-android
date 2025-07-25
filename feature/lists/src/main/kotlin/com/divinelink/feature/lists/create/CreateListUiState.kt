package com.divinelink.feature.lists.create

import com.divinelink.core.ui.snackbar.SnackbarMessage

data class CreateListUiState(
  val name: String,
  val description: String,
  val public: Boolean,
  val editMode: Boolean,
  val snackbarMessage: SnackbarMessage?,
) {
  companion object {
    val initial = CreateListUiState(
      name = "",
      description = "",
      public = true,
      editMode = false,
      snackbarMessage = null,
    )
  }
}
