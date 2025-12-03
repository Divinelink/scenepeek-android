package com.divinelink.core.model.jellyseerr.media

import com.divinelink.core.model.ItemState
import com.divinelink.core.model.media.MediaItem

data class RequestUiItem(
  val request: JellyseerrRequest,
  val mediaState: ItemState<MediaItem.Media>?,
)
