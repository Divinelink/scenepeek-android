package com.divinelink.core.network.awards.model.awards

import kotlinx.serialization.Serializable

@Serializable
data class AwardsResponse(
  val awards: List<AwardsYearResponse>,
)
