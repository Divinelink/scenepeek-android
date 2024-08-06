package com.divinelink.core.data.person.credits.mapper

import com.divinelink.core.database.person.credits.PersonCastCreditEntity
import com.divinelink.core.database.person.credits.PersonCrewCreditEntity
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.details.person.model.PersonCreditsApi

fun PersonCreditsApi.toEntityCast() = cast
  .filter { MediaType.isMedia(it.mediaType) }
  .map {
    PersonCastCreditEntity(
      id = it.id.toLong(),
      personId = this.id,
      adult = if (it.adult) 1 else 0,
      backdropPath = it.backdropPath,
      genreIds = it.genreIds.joinToString(", "),
      originCountry = it.originCountry?.joinToString(", "),
      originalLanguage = it.originalLanguage,
      originalTitle = it.originalTitle,
      originalName = it.originalName,
      overview = it.overview,
      popularity = it.popularity,
      posterPath = it.posterPath,
      releaseDate = it.releaseDate,
      firstAirDate = it.firstAirDate,
      title = it.title,
      name = it.name,
      video = if (it.video == true) 1 else 0,
      voteAverage = it.voteAverage,
      voteCount = it.voteCount,
      character = it.character,
      creditId = it.creditId,
      episodeCount = it.episodeCount?.toLong(),
      mediaType = it.mediaType,
    )
  }

fun PersonCreditsApi.toEntityCrew() = crew
  .filter { MediaType.isMedia(it.mediaType) }
  .map {
    PersonCrewCreditEntity(
      id = it.id.toLong(),
      personId = this.id,
      adult = if (it.adult) 1 else 0,
      backdropPath = it.backdropPath,
      genreIds = it.genreIds.joinToString(", "),
      originCountry = it.originCountry?.joinToString(", "),
      originalLanguage = it.originalLanguage,
      originalTitle = it.originalTitle,
      originalName = it.originalName,
      overview = it.overview,
      popularity = it.popularity,
      posterPath = it.posterPath,
      releaseDate = it.releaseDate,
      firstAirDate = it.firstAirDate,
      title = it.title,
      name = it.name,
      video = it.video?.let { video -> if (video) 1 else 0 },
      voteAverage = it.voteAverage,
      voteCount = it.voteCount,
      job = it.job,
      creditId = it.creditId,
      episodeCount = it.episodeCount?.toLong(),
      mediaType = it.mediaType,
      department = it.department,
    )
  }
