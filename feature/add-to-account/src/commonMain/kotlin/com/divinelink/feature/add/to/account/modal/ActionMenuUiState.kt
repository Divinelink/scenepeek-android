package com.divinelink.feature.add.to.account.modal

import com.divinelink.core.model.ItemState
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.track.UserShowStatus
import com.divinelink.core.ui.snackbar.SnackbarMessage

data class ActionMenuUiState(
  val mediaState: ItemState<MediaItem>,
  val entryPoint: ActionMenuEntryPoint,
  val snackbarMessage: SnackbarMessage?,
) {
  val media = (mediaState as? ItemState.Data)?.item

  companion object {
    fun initial(entryPoint: ActionMenuEntryPoint): ActionMenuUiState = ActionMenuUiState(
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
      ActionMenuSection.Intent.AddToList,
      ActionMenuSection.Intent.RemoveFromList,
      ActionMenuSection.Intent.MultiSelect,
      ActionMenuSection.Intent.Share,
    )
    is ActionMenuEntryPoint.Other -> listOf(
      ActionMenuSection.Intent.AddToList,
      ActionMenuSection.Intent.Share,
    )
    is ActionMenuEntryPoint.ShowProgress -> buildList<ActionMenuSection> {
      when (entryPoint.status) {
        UserShowStatus.WATCHING -> {
          add(ActionMenuSection.Intent.PauseShow)
          add(ActionMenuSection.Intent.DropShow)
        }
        UserShowStatus.COMPLETED -> Unit
        UserShowStatus.ON_HOLD -> {
          add(ActionMenuSection.Intent.ContinueWatching)
          add(ActionMenuSection.Intent.DropShow)
        }
        UserShowStatus.DROPPED -> {
          add(ActionMenuSection.Intent.ContinueWatching)
          add(ActionMenuSection.Intent.PauseShow)
        }
        UserShowStatus.PLAN_TO_WATCH -> {
          add(ActionMenuSection.Intent.ContinueWatching)
          add(ActionMenuSection.Intent.PauseShow)
          add(ActionMenuSection.Intent.DropShow)
        }
      }

      add(ActionMenuSection.Divider)
      add(ActionMenuSection.Intent.AddToList)
      add(ActionMenuSection.Intent.Share)
    }
  }
}

internal fun ActionMenuUiState.updateMedia(mediaItem: MediaItem) = copy(
  entryPoint = when (entryPoint) {
    is ActionMenuEntryPoint.ListDetails -> entryPoint.copy(media = mediaItem)
    is ActionMenuEntryPoint.Other -> entryPoint.copy(media = mediaItem)
    is ActionMenuEntryPoint.ShowProgress -> entryPoint
  },
  mediaState = ItemState.Data(
    item = mediaItem,
    loading = false,
  ),
)
