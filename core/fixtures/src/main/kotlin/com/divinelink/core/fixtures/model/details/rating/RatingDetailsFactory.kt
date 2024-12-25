package com.divinelink.core.fixtures.model.details.rating

import com.divinelink.core.model.details.rating.RatingDetails

object RatingDetailsFactory {

  fun tmdb(): RatingDetails = RatingDetails.Score(
    voteAverage = 7.5,
    voteCount = 1_234,
  )

  fun imdb(): RatingDetails = RatingDetails.Score(
    voteAverage = 8.5,
    voteCount = 2_345,
  )

  fun trakt(): RatingDetails = RatingDetails.Score(
    voteAverage = 9.5,
    voteCount = 3_456,
  )
}
