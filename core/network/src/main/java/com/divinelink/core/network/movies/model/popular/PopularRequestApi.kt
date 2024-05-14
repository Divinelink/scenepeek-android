package com.divinelink.core.network.movies.model.popular

import kotlinx.serialization.Serializable

@Serializable
data class PopularRequestApi(
  val page: Int,
)
