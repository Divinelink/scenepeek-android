package com.divinelink.core.network.session.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateRequestTokenResponseApi(
  val success: Boolean,
  @SerialName("expires_at") val expiresAt: String,
  @SerialName("request_token") val requestToken: String,
)
