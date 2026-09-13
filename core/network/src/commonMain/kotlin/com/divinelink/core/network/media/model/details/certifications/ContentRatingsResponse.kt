package com.divinelink.core.network.media.model.details.certifications

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ContentRatingsResponse(
  val results: List<ContentRatingResponse>,
) {
  @Serializable
  data class ContentRatingResponse(
    @SerialName("iso_3166_1") val iso31661: String,
    val rating: String,
  )
}
