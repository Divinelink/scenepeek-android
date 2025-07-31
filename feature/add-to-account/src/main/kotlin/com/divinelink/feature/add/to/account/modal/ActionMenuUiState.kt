package com.divinelink.feature.add.to.account.modal

import com.divinelink.core.model.media.MediaItem

data class ActionMenuUiState(
  val media: MediaItem,
  val availableActions: List<ActionMenuIntent>,
) {
  companion object {
    fun initial(
      media: MediaItem,
      entryPoint: ActionMenuEntryPoint,
    ): ActionMenuUiState = ActionMenuUiState(
      media = media,
      availableActions = when (entryPoint) {
        ActionMenuEntryPoint.ListDetails -> listOf(
          ActionMenuIntent.AddToList,
          ActionMenuIntent.RemoveFromList,
          ActionMenuIntent.MultiSelect,
          ActionMenuIntent.Share,
        )
        ActionMenuEntryPoint.Other -> listOf(
          ActionMenuIntent.Share,
        )
      },
    )
  }
}
