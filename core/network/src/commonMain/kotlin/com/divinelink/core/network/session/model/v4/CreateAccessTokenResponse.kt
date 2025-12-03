package com.divinelink.core.network.session.model.v4

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateAccessTokenResponse(
  @SerialName("access_token") val accessToken: String,
  @SerialName("account_id") val accountId: String,
)
