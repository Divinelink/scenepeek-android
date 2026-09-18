package com.divinelink.feature.add.to.account.modal

import com.divinelink.core.model.media.MediaItem

sealed interface ActionMenuEntryPoint {
  data class ListDetails(
    val media: MediaItem,
    val listId: Int,
    val listName: String,
  ) : ActionMenuEntryPoint

  data class Other(val media: MediaItem) : ActionMenuEntryPoint

  data class ShowProgress(val showId: Long) : ActionMenuEntryPoint
}
