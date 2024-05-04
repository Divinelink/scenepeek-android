package com.andreolas.movierama.base.data.remote.session.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateSessionRequestApi(
  @SerialName("request_token") val requestToken: String
)
