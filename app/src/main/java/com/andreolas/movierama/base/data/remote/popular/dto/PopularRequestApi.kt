package com.andreolas.movierama.base.data.remote.popular.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PopularRequestApi(
    @SerialName("api_key")
    val apiKey: String,
    val page: Int,
)
