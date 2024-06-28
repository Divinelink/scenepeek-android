package com.divinelink.core.model.details

import com.divinelink.core.model.details.crew.Actor
import com.divinelink.core.model.details.crew.Director

/**
 * Represents details of a TV show.
 */
data class TV(
  override val id: Int,
  override val title: String,
  override val posterPath: String,
  override val overview: String?,
  override val director: Director?,
  override val genres: List<String>?,
  override val cast: List<Actor>,
  override val releaseDate: String,
  override val rating: String,
  override val isFavorite: Boolean,
  val seasons: List<Nothing>? = null, // TODO
  val numberOfSeasons: Int,
) : MediaDetails()
