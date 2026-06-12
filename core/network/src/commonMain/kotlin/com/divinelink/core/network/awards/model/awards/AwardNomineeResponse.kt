package com.divinelink.core.network.awards.model.awards

import kotlinx.serialization.Serializable

@Serializable
data class AwardNomineeResponse(
  val id: Int,
  val winner: Boolean,
)
