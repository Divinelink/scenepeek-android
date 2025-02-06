package com.divinelink.core.navigation.route

import com.divinelink.core.model.media.MediaType
import kotlinx.serialization.Serializable

/**
 * Information required when launching the movie details screen.
 */
@Serializable
data class DetailsRoute(
  val id: Int,
  val mediaType: MediaType,
  val isFavorite: Boolean?,
)
