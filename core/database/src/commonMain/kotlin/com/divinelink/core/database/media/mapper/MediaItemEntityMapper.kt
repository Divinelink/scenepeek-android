package com.divinelink.core.database.media.mapper

import com.divinelink.core.database.MediaItemEntity
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType

fun MediaItemEntity.map() = when (MediaType.from(mediaType)) {
  MediaType.TV -> MediaItem.Media.TV(
    id = id,
    name = name,
    posterPath = posterPath,
    backdropPath = backdropPath,
    releaseDate = releaseDate ?: "",
    voteAverage = voteAverage,
    voteCount = voteCount.toInt(),
    overview = overview,
    popularity = popularity,
    saved = false,
    accountRating = null,
  )
  MediaType.MOVIE -> MediaItem.Media.Movie(
    id = id,
    name = name,
    posterPath = posterPath,
    backdropPath = backdropPath,
    releaseDate = releaseDate ?: "",
    voteAverage = voteAverage,
    voteCount = voteCount.toInt(),
    overview = overview,
    popularity = popularity,
    saved = false,
    accountRating = null,
  )
  else -> null
}
