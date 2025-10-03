package com.divinelink.core.network.media.model

import kotlinx.serialization.Serializable

@Serializable
data class GenreResponse(
  val id: Int,
  val name: String,
)
