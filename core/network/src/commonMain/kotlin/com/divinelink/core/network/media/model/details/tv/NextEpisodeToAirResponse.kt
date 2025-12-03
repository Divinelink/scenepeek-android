package com.divinelink.core.network.media.model.details.tv

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NextEpisodeToAirResponse(
  val id: Int,
  @SerialName("air_date") val airDate: String,
)
