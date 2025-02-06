package com.divinelink.core.navigation.route

import com.divinelink.core.model.media.MediaType
import kotlinx.serialization.Serializable

@Serializable
data class CreditsRoute(
  val id: Long,
  val mediaType: MediaType?,
)
