package com.divinelink.core.network.changes.model.api

import com.divinelink.core.network.changes.model.serializer.ChangeValue
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChangeItemApi(
  val id: String,
  val action: String,
  val time: String,
  @SerialName("iso_639_1") val iso6391: String,
  @SerialName("iso_3166_1") val iso31661: String,
  val value: ChangeValue?,
  @SerialName("original_value") val originalValue: ChangeValue?,
) {
  companion object {
    object JsonKeys {
      const val ID = "id"
      const val ACTION = "action"
      const val TIME = "time"
      const val ISO_639_1 = "iso_639_1"
      const val ISO_3166_1 = "iso_3166_1"
      const val VALUE = "value"
      const val ORIGINAL_VALUE = "original_value"
    }
  }
}
