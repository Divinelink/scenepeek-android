package com.divinelink.core.network.media.model.popular

import kotlinx.serialization.Serializable

@Serializable
data class PopularRequestApi(
  val page: Int,
)
