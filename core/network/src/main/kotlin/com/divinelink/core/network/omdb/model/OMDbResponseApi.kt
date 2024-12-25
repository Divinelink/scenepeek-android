package com.divinelink.core.network.omdb.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OMDbResponseApi(
  @SerialName("Metascore") val metascore: String,
  val imdbRating: String,
  val imdbVotes: String,
)
