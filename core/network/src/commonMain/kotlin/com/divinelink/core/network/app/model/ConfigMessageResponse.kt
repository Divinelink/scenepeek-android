package com.divinelink.core.network.app.model

import kotlinx.serialization.Serializable

@Serializable
data class ConfigMessageResponse(
  val id: String,
  val content: String,
  val visible: Boolean,
  val dismissable: Boolean,
)
