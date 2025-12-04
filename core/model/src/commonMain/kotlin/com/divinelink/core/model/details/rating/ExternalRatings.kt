package com.divinelink.core.model.details.rating

data class ExternalRatings(
  val imdb: RatingDetails,
  val rt: RatingDetails,
  val metascore: RatingDetails,
)
