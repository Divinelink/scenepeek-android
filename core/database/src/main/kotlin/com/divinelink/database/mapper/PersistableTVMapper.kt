package com.divinelink.database.mapper

import com.divinelink.core.model.media.MediaItem
import com.divinelink.database.model.PersistableTV

internal fun MediaItem.Media.TV.toPersistableTV() = PersistableTV(
  id = this.id,
  title = this.name,
  posterPath = this.posterPath ?: "",
  releaseDate = this.releaseDate,
  rating = this.rating,
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
  releaseDate = this.releaseDate,
  rating = this.rating,
  overview = this.overview,
  isFavorite = this.isFavorite,
)
