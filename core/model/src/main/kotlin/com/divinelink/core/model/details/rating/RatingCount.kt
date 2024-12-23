package com.divinelink.core.model.details.rating

data class RatingCount(
  val ratings: Map<RatingSource, RatingDetails?>,
) {
  companion object {
    fun initial(tmdbVoteAverage: Double, tmdbVoteCount: Int) = RatingCount(
      ratings = mapOf(
        RatingSource.TMDB to RatingDetails(
          voteAverage = tmdbVoteAverage,
          voteCount = tmdbVoteCount,
        ),
        RatingSource.IMDB to null,
        RatingSource.TRAKT to null,
      ),
    )
  }

  fun getRating(source: RatingSource): RatingDetails? = ratings[source]

  fun updateRating(source: RatingSource, rating: RatingDetails): RatingCount {
    return copy(ratings = ratings + (source to rating))
  }
}
