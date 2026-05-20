package com.divinelink.core.network.app.model

import kotlinx.serialization.Serializable

@Serializable
data class ConfigResponse(
  val messages: List<ConfigMessageResponse>?,
)
