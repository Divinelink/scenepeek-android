package com.divinelink.core.model.details

import com.divinelink.core.model.details.crew.Actor
import com.divinelink.core.model.details.crew.Director

/**
 * Represents details of a movie.
 */
data class Movie(
  override val id: Int,
  override val title: String,
  override val posterPath: String,
  override val overview: String?,
  override val genres: List<String>?,
  override val director: Director?,
  override val cast: List<Actor>,
  override val releaseDate: String,
  override val rating: String,
  val runtime: String?,
  override val isFavorite: Boolean,
) : MediaDetails()
