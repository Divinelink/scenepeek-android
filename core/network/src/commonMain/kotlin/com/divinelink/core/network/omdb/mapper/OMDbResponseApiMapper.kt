package com.divinelink.core.network.omdb.mapper

import com.divinelink.core.model.details.rating.ExternalRatings
import com.divinelink.core.model.details.rating.RatingDetails
import com.divinelink.core.model.details.rating.RatingSource
import com.divinelink.core.network.omdb.model.OMDbResponseApi

fun OMDbResponseApi.map(): ExternalRatings {
  val voteAverage = imdbRating.toDoubleOrNull()
  val voteCount = imdbVotes.replace(",", "").toIntOrNull()

  val imdb = if (voteAverage != null && voteCount != null) {
    RatingDetails.Score(
      voteAverage = voteAverage,
      voteCount = voteCount,
    )
  } else {
    RatingDetails.Unavailable
  }

  val rottenTomatoes = ratings.find { it.source == RatingSource.RT.value }
  val rtRating = rottenTomatoes?.value?.substringBefore("%")?.toIntOrNull()

  val metacritic = ratings.find { it.source == RatingSource.METACRITIC.value }
  val metascore = metacritic?.value?.substringBefore("/")?.toIntOrNull()

  return ExternalRatings(
    imdb = imdb,
    rt = rtRating?.let { value ->
      RatingDetails.Rating(value)
    } ?: RatingDetails.Unavailable,
    metascore = metascore?.let { value ->
      RatingDetails.Rating(value)
    } ?: RatingDetails.Unavailable,
  )
}
