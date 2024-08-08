package com.divinelink.core.data.person.credits.mapper

import com.divinelink.core.database.person.credits.PersonCastCreditEntity
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.person.credits.PersonMovieCastCredit
import com.divinelink.core.model.person.credits.PersonTVCastCredit

fun PersonCastCreditEntity.map() = when (MediaType.from(mediaType)) {
  MediaType.TV -> PersonTVCastCredit(
    id = id,
    name = name ?: "",
    originalName = originalName ?: "",
    posterPath = posterPath,
    mediaType = MediaType.from(mediaType),
    adult = adult.toInt() == 1,
    backdropPath = backdropPath,
    genreIds = genreIds.split(", ").filter { it.isNotBlank() }.map { it.toInt() },
    originalLanguage = originalLanguage,
    overview = overview,
    popularity = popularity,
    firstAirDate = firstAirDate!!,
    voteAverage = voteAverage,
    voteCount = voteCount,
    episodeCount = episodeCount?.toInt() ?: 0,
    creditId = creditId,
    character = character,
    originCountry = originCountry?.split(", ")?.map { it } ?: emptyList(),
  )
  else -> PersonMovieCastCredit(
    id = id,
    title = title ?: "",
    originalTitle = originalTitle ?: "",
    posterPath = posterPath,
    mediaType = MediaType.from(mediaType),
    adult = adult.toInt() == 1,
    backdropPath = backdropPath,
    genreIds = genreIds.split(", ").filter { it.isNotBlank() }.map { it.toInt() },
    originalLanguage = originalLanguage,
    overview = overview,
    popularity = popularity,
    releaseDate = releaseDate!!,
    voteAverage = voteAverage,
    voteCount = voteCount,
    creditId = creditId,
    video = video?.toInt() == 1,
    character = character,
    order = creditOrder?.toInt() ?: Int.MAX_VALUE,
  )
}
