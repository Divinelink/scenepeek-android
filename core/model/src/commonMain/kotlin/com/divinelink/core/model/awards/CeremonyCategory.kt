package com.divinelink.core.model.awards

import kotlinx.serialization.Serializable

@Serializable
data class CeremonyCategory(
  val id: String,
  val title: String,
)
