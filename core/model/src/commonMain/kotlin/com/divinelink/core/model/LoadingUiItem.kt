package com.divinelink.core.model

import com.divinelink.core.model.media.MediaItem

data class LoadingUiItem<T>(
  val item: T,
  val mediaState: ItemState<MediaItem>?,
)
