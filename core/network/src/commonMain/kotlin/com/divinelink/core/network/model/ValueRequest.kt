package com.divinelink.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ValueRequest(
  val value: Float,
)
