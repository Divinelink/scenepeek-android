package com.divinelink.core.database.media.mapper

import com.divinelink.core.database.MediaItemEntity
import com.divinelink.core.model.media.MediaItem

fun MediaItem.Media.map() = MediaItemEntity(
  id = id.toLong(),
  adult = -1,
  backdropPath = backdropPath,
  posterPath = posterPath,
  mediaType = mediaType.value,
  originalLanguage = "",
  popularity = popularity,
  voteAverage = voteAverage,
  voteCount = voteCount.toLong(),
  overview = overview,
  releaseDate = releaseDate,
  video = -1,
  name = name,
  originalName = name,
  firstAirDate = releaseDate,
  genreIdsJson = "",
  originCountryJson = "",
)
