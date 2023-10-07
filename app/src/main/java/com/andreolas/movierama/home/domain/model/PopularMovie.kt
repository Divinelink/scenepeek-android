package com.andreolas.movierama.home.domain.model

import kotlinx.serialization.Serializable

@Serializable
// FIXME maybe remove this altogether
@Deprecated("Use MediaItem.Media instead")
data class PopularMovie(
  val id: Int,
  val posterPath: String,
  val releaseDate: String,
  val title: String,
  val rating: String,
  val overview: String,
  val isFavorite: Boolean,
)
