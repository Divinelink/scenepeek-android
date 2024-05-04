package com.andreolas.movierama.base.data.remote.session.dto.delete

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeleteSessionRequestApi(
  @SerialName("session_id") val sessionId: String
)
