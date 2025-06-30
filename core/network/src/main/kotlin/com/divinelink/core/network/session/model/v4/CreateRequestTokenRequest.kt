package com.divinelink.core.network.session.model.v4

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateRequestTokenRequest(@SerialName("redirect_to") val redirectTo: String)
