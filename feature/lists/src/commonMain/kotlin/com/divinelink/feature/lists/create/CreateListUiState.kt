package com.divinelink.feature.lists.create

import com.divinelink.core.ui.snackbar.SnackbarMessage

data class CreateListUiState(
  val id: Int,
  val name: String,
  val description: String,
  val public: Boolean,
  val editMode: Boolean,
  val backdrop: String?,
  val snackbarMessage: SnackbarMessage?,
  val loading: Boolean,
) {
  companion object {
    val initial = CreateListUiState(
      id = -1,
      name = "",
      description = "",
      public = true,
      backdrop = "",
      editMode = false,
      snackbarMessage = null,
      loading = false,
    )
  }
}
