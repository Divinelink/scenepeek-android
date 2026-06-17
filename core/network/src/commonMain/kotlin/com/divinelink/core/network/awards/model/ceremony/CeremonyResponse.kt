package com.divinelink.core.network.awards.model.ceremony

import kotlinx.serialization.Serializable

@Serializable
data class CeremonyResponse(
  val id: String,
  val title: String,
  val imageUrl: String,
)
