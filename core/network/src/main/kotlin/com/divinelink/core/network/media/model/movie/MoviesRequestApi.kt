package com.divinelink.core.network.media.model.movie

import kotlinx.serialization.Serializable

@Serializable
data class MoviesRequestApi(
  val page: Int,
)
