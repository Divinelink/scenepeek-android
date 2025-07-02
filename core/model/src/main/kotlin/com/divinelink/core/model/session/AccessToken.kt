package com.divinelink.core.model.session

import kotlinx.serialization.Serializable

@Serializable
data class AccessToken(
  val accessToken: String,
  val accountId: String,
)
