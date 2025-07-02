package com.divinelink.core.network.session.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateRequestTokenResponseApi(
  val success: Boolean,
  @SerialName("request_token") val requestToken: String,
)
