package com.divinelink.core.network.session.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateSessionRequestApi(@SerialName("request_token") val requestToken: String)
