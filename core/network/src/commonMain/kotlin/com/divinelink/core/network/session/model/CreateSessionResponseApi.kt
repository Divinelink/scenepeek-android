package com.divinelink.core.network.session.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateSessionResponseApi(
  val success: Boolean,
  @SerialName("session_id") val sessionId: String,
)
