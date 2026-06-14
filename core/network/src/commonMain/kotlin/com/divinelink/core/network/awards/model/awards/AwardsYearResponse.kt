package com.divinelink.core.network.awards.model.awards

import kotlinx.serialization.Serializable

@Serializable
data class AwardsYearResponse(
  val year: String,
  val movies: List<AwardNomineeResponse>?,
  val persons: List<AwardNomineeResponse>?,
  val shows: List<AwardNomineeResponse>?,
)
