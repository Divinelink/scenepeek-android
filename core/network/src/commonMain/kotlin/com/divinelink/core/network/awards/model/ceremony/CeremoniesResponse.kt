package com.divinelink.core.network.awards.model.ceremony

import kotlinx.serialization.Serializable

@Serializable
data class CeremoniesResponse(
  val ceremonies: List<CeremonyResponse>,
)
