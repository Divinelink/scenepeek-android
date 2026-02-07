package com.divinelink.core.network.media.model.states

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EpisodeRatingResponse(
  val id: Int,
  @SerialName("episode_number") val episodeNumber: Int,
  val rated: RateResponseApi,
)
