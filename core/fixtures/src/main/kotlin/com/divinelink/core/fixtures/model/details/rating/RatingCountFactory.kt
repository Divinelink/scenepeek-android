package com.divinelink.core.fixtures.model.details.rating

import com.divinelink.core.model.details.rating.RatingCount
import com.divinelink.core.model.details.rating.RatingSource

object RatingCountFactory {

  fun tmdb(): RatingCount = RatingCount(
    ratings = mapOf(
      RatingSource.TMDB to RatingDetailsFactory.tmdb(),
      RatingSource.IMDB to null,
      RatingSource.TRAKT to null,
    ),
  )

  fun imdb(): RatingCount = RatingCount(
    ratings = mapOf(
      RatingSource.TMDB to RatingDetailsFactory.tmdb(),
      RatingSource.IMDB to RatingDetailsFactory.imdb(),
      RatingSource.TRAKT to null,
    ),
  )

  fun trakt(): RatingCount = RatingCount(
    ratings = mapOf(
      RatingSource.TMDB to RatingDetailsFactory.tmdb(),
      RatingSource.IMDB to null,
      RatingSource.TRAKT to RatingDetailsFactory.trakt(),
    ),
  )

  fun full(): RatingCount = RatingCount(
    ratings = mapOf(
      RatingSource.TMDB to RatingDetailsFactory.tmdb(),
      RatingSource.IMDB to RatingDetailsFactory.imdb(),
      RatingSource.TRAKT to RatingDetailsFactory.trakt(),
    ),
  )

  fun all(): List<RatingCount> = listOf(tmdb(), imdb(), trakt(), full())
}
