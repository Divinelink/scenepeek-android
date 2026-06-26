package com.divinelink.core.network.awards.model.awards

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AwardNomineeResponse(
  val id: Long,
  val winner: Boolean,
  @SerialName("media_id") val mediaId: Long?,
  @SerialName("media_type") val type: String?,
  @SerialName("media_title") val title: String?,
  val countries: List<String>?,
)
