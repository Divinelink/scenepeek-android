package com.divinelink.core.network.trakt.mapper

import com.divinelink.core.model.details.rating.RatingDetails
import com.divinelink.core.network.trakt.model.TraktRatingApi

// TODO Add tests
fun TraktRatingApi.map(): RatingDetails = RatingDetails.Score(
  voteAverage = rating,
  voteCount = votes,
)
