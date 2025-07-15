package com.divinelink.core.model.list

import com.divinelink.core.model.media.MediaItem

data class ListDetails(
  val page: Int,
  val name: String,
  val media: List<MediaItem.Media>,
  val totalPages: Int,
  val totalResults: Int,
) {
  fun canLoadMore(): Boolean = page < totalPages
  fun isEmpty(): Boolean = media.isEmpty()
}
