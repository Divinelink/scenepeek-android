package com.divinelink.core.network.list.model.remove

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoveItemsResponse(
  val success: Boolean,
  val results: List<RemoveItemResponse>,
) {
  @Serializable
  data class RemoveItemResponse(
    @SerialName("media_type") val mediaType: String,
    @SerialName("media_id") val mediaId: Int,
    val success: Boolean,
  )
}
