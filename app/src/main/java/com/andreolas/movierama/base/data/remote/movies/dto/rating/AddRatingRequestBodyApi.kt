package com.andreolas.movierama.base.data.remote.movies.dto.rating

import kotlinx.serialization.Serializable

@Serializable
data class AddRatingRequestBodyApi(
  val value: Int
)
