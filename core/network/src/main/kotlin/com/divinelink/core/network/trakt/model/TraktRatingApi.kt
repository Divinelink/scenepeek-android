package com.divinelink.core.network.trakt.model

import kotlinx.serialization.Serializable

@Serializable
data class TraktRatingApi(
  val rating: Double,
  val votes: Int,
)
