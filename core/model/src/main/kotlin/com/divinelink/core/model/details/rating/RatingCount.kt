package com.divinelink.core.model.details.rating

data class RatingCount(val ratings: Map<RatingSource, RatingDetails>) {

  companion object {
    fun tmdb(
      tmdbVoteAverage: Double,
      tmdbVoteCount: Int,
    ) = RatingCount(
      ratings = mapOf(
        RatingSource.TMDB to RatingDetails.Score(
          voteAverage = tmdbVoteAverage,
          voteCount = tmdbVoteCount,
        ),
        RatingSource.IMDB to RatingDetails.Initial,
        RatingSource.TRAKT to RatingDetails.Initial,
      ),
    )
  }

  // TODO Check if this is correct
  fun getRatingDetails(source: RatingSource): RatingDetails =
    ratings[source] ?: RatingDetails.Unavailable

  fun getRating(source: RatingSource): RatingDetails.Score? = // TODO Check if this is needed
    ratings[source] as? RatingDetails.Score

  fun updateRating(
    source: RatingSource,
    rating: RatingDetails,
  ): RatingCount = copy(ratings = ratings + (source to rating))
}
