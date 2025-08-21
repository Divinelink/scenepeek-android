package com.divinelink.core.data.person.credits.mapper

import com.divinelink.core.database.person.credits.CrewCreditsWithMedia
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.person.credits.PersonCredit

fun CrewCreditsWithMedia.map(
  isTvFavorite: Boolean,
  isMovieFavorite: Boolean,
) = when (MediaType.from(mediaType)) {
  MediaType.TV -> PersonCredit(
    media = MediaItem.Media.TV(
      id = id.toInt(),
      name = name,
      posterPath = posterPath,
      backdropPath = backdropPath,
      releaseDate = firstAirDate!!,
      voteAverage = voteAverage,
      voteCount = voteCount.toInt(),
      overview = overview,
      popularity = popularity,
      isFavorite = isTvFavorite,
    ),
    creditId = creditId,
    role = PersonRole.Crew(
      job = job,
      creditId = creditId,
      department = department,
      totalEpisodes = episodeCount,
    ),
  )
  else -> PersonCredit(
    media = MediaItem.Media.Movie(
      id = id.toInt(),
      name = name,
      posterPath = posterPath,
      backdropPath = backdropPath,
      releaseDate = releaseDate!!,
      voteAverage = voteAverage,
      voteCount = voteCount.toInt(),
      overview = overview,
      popularity = popularity,
      isFavorite = isMovieFavorite,
    ),
    creditId = creditId,
    role = PersonRole.Crew(
      job = job,
      creditId = creditId,
      department = department,
    ),
  )
}
