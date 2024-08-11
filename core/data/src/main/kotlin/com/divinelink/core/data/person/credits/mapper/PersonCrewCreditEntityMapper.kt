package com.divinelink.core.data.person.credits.mapper

import com.divinelink.core.database.person.credits.PersonCrewCreditEntity
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.person.credits.PersonMovieCrewCredit
import com.divinelink.core.model.person.credits.PersonTVCrewCredit

fun PersonCrewCreditEntity.map() = when (MediaType.from(mediaType)) {
  MediaType.TV -> PersonTVCrewCredit(
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
    job = job,
    episodeCount = episodeCount ?: 0,
    department = department,
    originCountry = originCountry?.split(", ")?.filter { it.isNotBlank() } ?: emptyList(),
    creditId = creditId,
  )
  else -> PersonMovieCrewCredit(
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
    job = job,
    department = department,
    creditId = creditId,
  )
}
