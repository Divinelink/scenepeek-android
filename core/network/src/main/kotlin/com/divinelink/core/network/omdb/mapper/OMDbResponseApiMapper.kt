package com.divinelink.core.network.omdb.mapper

import com.divinelink.core.model.details.rating.RatingDetails
import com.divinelink.core.network.omdb.model.OMDbResponseApi

fun OMDbResponseApi.map(): RatingDetails {
  val voteAverage = imdbRating.toDoubleOrNull()
  val voteCount = imdbVotes.replace(",", "").toIntOrNull()

  return if (voteAverage != null && voteCount != null) {
    RatingDetails.Score(
      voteAverage = voteAverage,
      voteCount = voteCount,
    )
  } else {
    RatingDetails.Unavailable
  }
}
