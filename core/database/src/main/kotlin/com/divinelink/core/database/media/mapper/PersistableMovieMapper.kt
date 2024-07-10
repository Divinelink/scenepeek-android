package com.divinelink.core.database.media.mapper

import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.database.media.model.PersistableMovie

internal fun MediaItem.Media.toPersistableMovie(): PersistableMovie = PersistableMovie(
  id = this.id,
  title = this.name,
  posterPath = this.posterPath ?: "",
  releaseDate = this.releaseDate,
  rating = this.rating,
  isFavorite = this.isFavorite ?: false,
  overview = this.overview,
)

fun List<PersistableMovie>.map(): List<MediaItem.Media> = this.map(PersistableMovie::toMovie)

private fun PersistableMovie.toMovie(): MediaItem.Media.Movie = MediaItem.Media.Movie(
  id = this.id,
  posterPath = this.posterPath,
  releaseDate = this.releaseDate,
  name = this.title,
  rating = this.rating,
  overview = this.overview,
  isFavorite = this.isFavorite,
)
