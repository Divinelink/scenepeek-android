package com.divinelink.feature.add.to.account.modal

import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.ui.snackbar.SnackbarMessage

data class ActionMenuUiState(
  val media: MediaItem,
  val entryPoint: ActionMenuEntryPoint,
  val snackbarMessage: SnackbarMessage?,
) {
  companion object {
    fun initial(
      media: MediaItem,
      entryPoint: ActionMenuEntryPoint,
    ): ActionMenuUiState = ActionMenuUiState(
      media = media,
      entryPoint = entryPoint,
      snackbarMessage = null,
    )
  }

  val availableActions = when (entryPoint) {
    is ActionMenuEntryPoint.ListDetails -> listOf(
      ActionMenuIntent.AddToList,
      ActionMenuIntent.RemoveFromList,
      ActionMenuIntent.MultiSelect,
      ActionMenuIntent.Share,
    )
    ActionMenuEntryPoint.Other -> listOf(
      ActionMenuIntent.Share,
    )
  }
}
