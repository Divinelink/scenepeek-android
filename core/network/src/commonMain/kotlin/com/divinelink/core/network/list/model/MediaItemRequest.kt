package com.divinelink.core.network.list.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MediaItemRequest(
  @SerialName("media_id") val mediaId: Int,
  @SerialName("media_type") val mediaType: String,
)
