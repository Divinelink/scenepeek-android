package com.divinelink.core.model.user.data

import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType

data class UserDataResponse(
  val data: List<MediaItem.Media>,
  val totalResults: Int,
  val type: MediaType,
  val canFetchMore: Boolean,
)
