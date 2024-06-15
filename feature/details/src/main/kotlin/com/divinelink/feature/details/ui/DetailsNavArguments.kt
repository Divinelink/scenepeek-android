package com.divinelink.feature.details.ui

/**
 * Information required when launching the movie details screen.
 */
data class DetailsNavArguments(
  val id: Int,
  val mediaType: String,
  val isFavorite: Boolean?,
)
