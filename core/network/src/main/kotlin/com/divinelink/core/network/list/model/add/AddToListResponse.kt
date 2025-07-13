package com.divinelink.core.network.list.model.add

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddToListResponse(val results: List<MediaItemResponse>) {
  @Serializable
  data class MediaItemResponse(
    @SerialName("media_id") val mediaId: Int,
    @SerialName("media_type") val mediaType: String,
    val success: Boolean,
    val error: List<String>? = null,
  )

  companion object {
    const val ALREADY_EXISTS_ERROR = "Media has already been taken"
  }
}
