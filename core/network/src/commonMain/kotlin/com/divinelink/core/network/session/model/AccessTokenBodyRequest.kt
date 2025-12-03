package com.divinelink.core.network.session.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccessTokenBodyRequest(@SerialName("access_token") val accessToken: String)
