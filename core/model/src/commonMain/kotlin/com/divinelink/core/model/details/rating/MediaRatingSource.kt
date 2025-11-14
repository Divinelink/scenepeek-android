package com.divinelink.core.model.details.rating

sealed class MediaRatingSource(open val options: List<RatingSource>) {
  data object Movie : MediaRatingSource(
    options = listOf(
      RatingSource.TMDB,
      RatingSource.IMDB,
      RatingSource.TRAKT,
    ),
  )

  data object TVShow : MediaRatingSource(
    options = listOf(
      RatingSource.TMDB,
      RatingSource.IMDB,
      RatingSource.TRAKT,
    ),
  )

  data object Episodes : MediaRatingSource(
    options = listOf(
      RatingSource.IMDB,
      RatingSource.TRAKT,
    ),
  )

  data object Seasons : MediaRatingSource(
    options = listOf(
      RatingSource.IMDB,
      RatingSource.TRAKT,
    ),
  )
}
