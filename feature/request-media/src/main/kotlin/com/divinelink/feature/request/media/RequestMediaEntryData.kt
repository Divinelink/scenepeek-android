package com.divinelink.feature.request.media

import com.divinelink.core.model.jellyseerr.media.JellyseerrRequest
import com.divinelink.core.model.media.MediaItem
import kotlinx.serialization.Serializable

@Serializable
data class RequestMediaEntryData(
  val request: JellyseerrRequest?,
  val media: MediaItem.Media,
)
