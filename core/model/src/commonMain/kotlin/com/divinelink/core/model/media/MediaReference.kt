package com.divinelink.core.model.media

import kotlinx.serialization.Serializable

@Serializable
data class MediaReference(
  val mediaId: Long,
  val mediaType: MediaType,
)
