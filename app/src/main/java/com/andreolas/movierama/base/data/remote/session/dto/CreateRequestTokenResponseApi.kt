package com.andreolas.movierama.base.data.remote.session.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateRequestTokenResponseApi(
  val success: Boolean,
  @SerialName("expires_at") val expiresAt: String,
  @SerialName("request_token") val requestToken: String,
)
