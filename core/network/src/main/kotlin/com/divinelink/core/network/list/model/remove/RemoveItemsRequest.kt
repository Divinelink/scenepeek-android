package com.divinelink.core.network.list.model.remove

import com.divinelink.core.model.media.MediaReference
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoveItemsRequest(val items: List<RemoveMediaRequest>) {

  @Serializable
  data class RemoveMediaRequest(
    @SerialName("media_type") val mediaType: String,
    @SerialName("media_id") val mediaId: Int,
  )

  companion object {
    fun fromMediaReference(items: List<MediaReference>): RemoveItemsRequest = RemoveItemsRequest(
      items = items.map { (id, type) ->
        RemoveMediaRequest(
          mediaType = type.value,
          mediaId = id,
        )
      },
    )
  }
}
