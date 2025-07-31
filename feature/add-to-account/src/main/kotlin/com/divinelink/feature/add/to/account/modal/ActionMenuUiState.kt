package com.divinelink.feature.add.to.account.modal

import com.divinelink.core.model.media.MediaItem

data class ActionMenuUiState(
  val media: MediaItem,
  val entryPoint: ActionMenuEntryPoint,
) {

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

  companion object {
    fun initial(
      media: MediaItem,
      entryPoint: ActionMenuEntryPoint,
    ): ActionMenuUiState = ActionMenuUiState(
      media = media,
      entryPoint = entryPoint,
    )
  }
}
