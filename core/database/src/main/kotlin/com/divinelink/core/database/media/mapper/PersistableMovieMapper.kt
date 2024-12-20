package com.divinelink.core.database.media.mapper

import com.divinelink.core.database.media.model.PersistableMovie
import com.divinelink.core.model.media.MediaItem

internal fun MediaItem.Media.toPersistableMovie(): PersistableMovie = PersistableMovie(
  id = this.id,
  title = this.name,
  posterPath = this.posterPath ?: "",
  releaseDate = this.releaseDate,
  voteAverage = this.voteAverage,
  voteCount = this.voteCount,
  isFavorite = this.isFavorite ?: false,
  overview = this.overview,
)

fun List<PersistableMovie>.map(): List<MediaItem.Media> = this.map(PersistableMovie::toMovie)

private fun PersistableMovie.toMovie(): MediaItem.Media.Movie = MediaItem.Media.Movie(
  id = this.id,
  posterPath = this.posterPath,
  releaseDate = this.releaseDate,
  name = this.title,
  voteAverage = this.voteAverage,
  voteCount = this.voteCount,
  overview = this.overview,
  isFavorite = this.isFavorite,
)
