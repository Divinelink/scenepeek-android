package com.divinelink.core.network.media.mapper.find

import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.person.Gender
import com.divinelink.core.network.media.model.find.FindByIdResponseApi
import com.divinelink.core.network.media.model.find.MovieResultApi
import com.divinelink.core.network.media.model.find.PersonResultApi
import com.divinelink.core.network.media.model.find.TvResultApi

fun FindByIdResponseApi.map(): MediaItem = when {
  movieResults.isNotEmpty() -> movieResults.first().map()
  tvResults.isNotEmpty() -> tvResults.first().map()
  personResults.isNotEmpty() -> personResults.first().map()
  tvEpisodeResults.isNotEmpty() -> MediaItem.Unknown
  else -> MediaItem.Unknown
}

private fun MovieResultApi.map(): MediaItem = MediaItem.Media.Movie(
  id = id,
  posterPath = posterPath,
  releaseDate = releaseDate,
  name = title,
  voteAverage = voteAverage,
  voteCount = voteCount,
  overview = overview,
  isFavorite = false,
)

private fun TvResultApi.map(): MediaItem = MediaItem.Media.TV(
  id = id,
  posterPath = posterPath,
  releaseDate = firstAirDate,
  name = name,
  voteAverage = voteAverage,
  voteCount = voteCount,
  overview = overview,
  isFavorite = false,
)

private fun PersonResultApi.map(): MediaItem = MediaItem.Person(
  id = id,
  profilePath = profilePath,
  name = name,
  gender = Gender.from(gender),
  knownForDepartment = knownForDepartment,
)
