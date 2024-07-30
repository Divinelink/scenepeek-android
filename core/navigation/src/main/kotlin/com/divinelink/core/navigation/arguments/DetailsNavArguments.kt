package com.divinelink.core.navigation.arguments

/**
 * Information required when launching the movie details screen.
 */
data class DetailsNavArguments(
  val id: Int,
  val mediaType: String,
  val isFavorite: Boolean?,
)
