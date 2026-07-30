package com.divinelink.core.model.media

import kotlinx.serialization.Serializable

@Serializable
data class MediaReference(
  val mediaId: Long,
  val mediaType: MediaType,
  val title: String? = null,
) {
  companion object {
    fun from(mediaId: Long, mediaType: String) = MediaReference(
      mediaId = mediaId,
      mediaType = MediaType.from(mediaType),
    )
  }
}
