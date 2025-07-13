package com.divinelink.core.model.list

import com.divinelink.core.model.media.MediaItem

data class ListDetails(
  val name: String,
  val media: List<MediaItem>,
)
