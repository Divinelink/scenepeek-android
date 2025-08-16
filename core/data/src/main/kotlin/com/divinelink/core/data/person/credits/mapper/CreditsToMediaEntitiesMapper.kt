package com.divinelink.core.data.person.credits.mapper

import com.divinelink.core.database.MediaItemEntity
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.details.person.model.PersonCreditsApi

fun PersonCreditsApi.toMediaEntities() = cast
  .filter { MediaType.isMedia(it.mediaType) }
  .map { response ->
    MediaItemEntity(
      id = response.id.toLong(),
      adult = -1,
      backdropPath = response.backdropPath,
      posterPath = response.posterPath,
      mediaType = response.mediaType,
      originalLanguage = response.originalLanguage,
      popularity = response.popularity,
      voteAverage = response.voteAverage,
      voteCount = response.voteCount,
      overview = response.overview,
      releaseDate = response.releaseDate,
      video = -1,
      name = response.name ?: response.title ?: "",
      originalName = response.originalName ?: response.originalTitle ?: "",
      firstAirDate = response.firstAirDate,
      originCountryJson = "",
      genreIdsJson = "",
    )
  }.plus(
    crew
      .filter { MediaType.isMedia(it.mediaType) }
      .map { response ->
        MediaItemEntity(
          id = response.id.toLong(),
          adult = -1,
          name = response.name ?: response.title ?: "",
          originalName = response.originalName ?: response.originalTitle ?: "",
          backdropPath = response.backdropPath,
          posterPath = response.posterPath,
          mediaType = response.mediaType,
          originalLanguage = response.originalLanguage,
          popularity = response.popularity,
          voteAverage = response.voteAverage,
          voteCount = response.voteCount,
          overview = response.overview,
          releaseDate = response.releaseDate,
          video = -1,
          firstAirDate = response.firstAirDate,
          originCountryJson = "",
          genreIdsJson = "",
        )
      },
  )
