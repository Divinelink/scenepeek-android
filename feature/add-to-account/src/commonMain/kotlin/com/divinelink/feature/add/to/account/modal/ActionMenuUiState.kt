package com.divinelink.feature.add.to.account.modal

import com.divinelink.core.model.ItemState
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.ui.snackbar.SnackbarMessage

data class ActionMenuUiState(
  val mediaState: ItemState<MediaItem>,
  val entryPoint: ActionMenuEntryPoint,
  val snackbarMessage: SnackbarMessage?,
) {
  val media = (mediaState as? ItemState.Data)?.item

  companion object {
    fun initial(
      entryPoint: ActionMenuEntryPoint,
    ): ActionMenuUiState = ActionMenuUiState(
      mediaState = when (entryPoint) {
        is ActionMenuEntryPoint.ListDetails -> ItemState.Data(
          item = entryPoint.media,
          loading = false,
        )
        is ActionMenuEntryPoint.Other -> ItemState.Data(
          item = entryPoint.media,
          loading = false,
        )
        is ActionMenuEntryPoint.ShowProgress -> ItemState.Loading
      },
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
    is ActionMenuEntryPoint.Other -> listOf(
      ActionMenuIntent.AddToList,
      ActionMenuIntent.Share,
    )
    is ActionMenuEntryPoint.ShowProgress -> listOf(
      ActionMenuIntent.PauseShow,
      ActionMenuIntent.AddToList,
      ActionMenuIntent.Share,
    )
  }
}

internal fun ActionMenuUiState.updateMedia(
  mediaItem: MediaItem,
) = copy(
  entryPoint = when (entryPoint) {
    is ActionMenuEntryPoint.ListDetails -> entryPoint.copy(media = mediaItem)
    is ActionMenuEntryPoint.Other -> entryPoint.copy(media = mediaItem)
    is ActionMenuEntryPoint.ShowProgress -> entryPoint
  },
)
