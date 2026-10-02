package com.divinelink.core.model.certification

import kotlinx.serialization.Serializable

@Serializable
data class Certification(
  val certification: String,
  val order: Int,
)
