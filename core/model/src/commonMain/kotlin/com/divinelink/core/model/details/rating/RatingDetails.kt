package com.divinelink.core.model.details.rating

sealed interface RatingDetails {

  data object Initial : RatingDetails

  data object Unavailable : RatingDetails

  data class Score(
    val voteAverage: Double,
    val voteCount: Int,
  ) : RatingDetails

  data class Rating(
    val value: Int,
  ) : RatingDetails
}
