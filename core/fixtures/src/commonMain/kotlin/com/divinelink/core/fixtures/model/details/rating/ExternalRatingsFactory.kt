package com.divinelink.core.fixtures.model.details.rating

import com.divinelink.core.model.details.rating.ExternalRatings
import com.divinelink.core.model.details.rating.RatingDetails

object ExternalRatingsFactory {

  val all = ExternalRatings(
    imdb = RatingDetailsFactory.imdb(),
    rt = RatingDetailsFactory.rottenTomatoes,
    metascore = RatingDetailsFactory.metacritic,
  )

  val imdbOnly = ExternalRatings(
    imdb = RatingDetailsFactory.imdb(),
    rt = RatingDetails.Unavailable,
    metascore = RatingDetails.Unavailable,
  )
}
