package com.divinelink.core.database.media.mapper

import com.divinelink.core.database.MediaItemEntity
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType

fun MediaItemEntity.map() = when (MediaType.from(mediaType)) {
  MediaType.TV -> MediaItem.Media.TV(
    id = id.toInt(),
    name = name ?: "",
    posterPath = posterPath,
    backdropPath = backdropPath,
    releaseDate = releaseDate ?: "",
    voteAverage = voteAverage,
    voteCount = voteCount.toInt(),
    overview = overview,
    isFavorite = false,
    accountRating = null,
  )
  MediaType.MOVIE -> MediaItem.Media.Movie(
    id = id.toInt(),
    name = name ?: "",
    posterPath = posterPath,
    backdropPath = backdropPath,
    releaseDate = releaseDate ?: "",
    voteAverage = voteAverage,
    voteCount = voteCount.toInt(),
    overview = overview,
    isFavorite = false,
    accountRating = null,
  )
  else -> null
}
