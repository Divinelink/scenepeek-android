package com.divinelink.core.database.media.mapper

import com.divinelink.core.database.media.model.PersistableTV
import com.divinelink.core.model.media.MediaItem

internal fun MediaItem.Media.TV.toPersistableTV() = PersistableTV(
  id = this.id,
  title = this.name,
  posterPath = this.posterPath ?: "",
  backdropPath = this.backdropPath ?: "",
  releaseDate = this.releaseDate,
  voteAverage = this.voteAverage,
  voteCount = this.voteCount,
  isFavorite = this.isFavorite ?: false,
  overview = this.overview,
)

fun List<PersistableTV>.map(): List<MediaItem.Media> = this.map(
  PersistableTV::toTV,
)

internal fun PersistableTV.toTV() = MediaItem.Media.TV(
  id = this.id,
  name = this.title,
  posterPath = this.posterPath,
  backdropPath = this.backdropPath,
  releaseDate = this.releaseDate,
  voteAverage = this.voteAverage,
  voteCount = this.voteCount,
  overview = this.overview,
  isFavorite = this.isFavorite,
)
