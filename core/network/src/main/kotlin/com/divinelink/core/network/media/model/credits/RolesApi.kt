package com.divinelink.core.network.media.model.credits

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RolesApi(
  @SerialName("credit_id") val creditId: String,
  val character: String,
  @SerialName("episode_count") val episodeCount: Int,
)
