package com.divinelink.core.network.media.model.credits

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JobsApi(
  @SerialName("credit_id") val creditId: String,
  val job: String,
  @SerialName("episode_count") val episodeCount: Int,
)
