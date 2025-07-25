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
  popularity = 0.0,
  voteAverage = voteAverage,
  voteCount = voteCount.toLong(),
  overview = overview,
  title = name,
  originalTitle = name,
  releaseDate = releaseDate,
  video = -1,
  name = name,
  originalName = name,
  firstAirDate = "",
  genreIdsJson = "",
  originCountryJson = "",
)
