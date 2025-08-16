package com.divinelink.core.data.person.credits.mapper

import com.divinelink.core.database.person.credits.CastCreditsWithMedia
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.person.credits.PersonCredit

fun CastCreditsWithMedia.map() = when (MediaType.from(mediaType)) {
  MediaType.TV -> PersonCredit(
    creditId = creditId,
    media = MediaItem.Media.TV(
      id = id.toInt(),
      name = name ?: "",
      posterPath = posterPath,
      backdropPath = backdropPath,
      releaseDate = firstAirDate!!,
      voteAverage = voteAverage,
      voteCount = voteCount.toInt(),
      overview = overview,
      popularity = popularity,
      isFavorite = false,
      accountRating = null,
    ),
    role = PersonRole.SeriesActor(
      character = character,
      creditId = creditId,
      totalEpisodes = episodeCount?.toInt() ?: 0,
    ),
  )
  else -> PersonCredit(
    creditId = creditId,
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
      isFavorite = false,
      accountRating = null,
    ),
    role = PersonRole.MovieActor(
      character = character,
      order = creditOrder?.toInt() ?: Int.MAX_VALUE,
    ),
  )
}
