package com.divinelink.core.network.movies.model.rating

import kotlinx.serialization.Serializable

@Serializable
data class AddRatingRequestBodyApi(
  val value: Int
)
