package com.andreolas.movierama.base.data.remote.session.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateSessionResponseApi(
  val success: Boolean,
  @SerialName("session_id") val sessionId: String
)
