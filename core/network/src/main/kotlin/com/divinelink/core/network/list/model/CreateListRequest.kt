package com.divinelink.core.network.list.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateListRequest(
  val name: String,
  val description: String,
  val public: Int,
  @SerialName("iso_3166_1") val iso31661: String,
  @SerialName("iso_639_1") val iso6391: String,
) {
  companion object {
    fun create(
      name: String,
      description: String,
      public: Boolean,
      iso31661: String = "US",
      iso6391: String = "en",
    ): CreateListRequest = CreateListRequest(
      name = name,
      description = description,
      public = if (public) 1 else 0,
      iso31661 = iso31661,
      iso6391 = iso6391,
    )
  }
}
