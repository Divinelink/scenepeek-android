package com.divinelink.core.model.details.rating

data class RatingCount(val ratings: Map<RatingSource, RatingDetails>) {

  companion object {
    fun tmdb(
      tmdbVoteAverage: Double,
      tmdbVoteCount: Int,
    ) = RatingCount(
      ratings = RatingSource.entries.associateWith { source ->
        when (source) {
          RatingSource.TMDB -> RatingDetails.Score(
            voteAverage = tmdbVoteAverage,
            voteCount = tmdbVoteCount,
          )
          RatingSource.IMDB -> RatingDetails.Initial
          RatingSource.TRAKT -> RatingDetails.Initial
          RatingSource.RT -> RatingDetails.Initial
          RatingSource.METACRITIC -> RatingDetails.Initial
        }
      },
    )
  }

  fun getRatingDetails(source: RatingSource): RatingDetails =
    ratings[source] ?: RatingDetails.Unavailable

  fun getRating(source: RatingSource): RatingDetails.Score? =
    ratings[source] as? RatingDetails.Score

  fun updateRating(
    source: RatingSource,
    rating: RatingDetails,
  ): RatingCount = copy(ratings = ratings + (source to rating))

  fun updateExternalRatings(externalRatings: ExternalRatings?): RatingCount {
    val metascore = externalRatings?.metascore ?: RatingDetails.Unavailable
    val rt = externalRatings?.rt ?: RatingDetails.Unavailable
    val imdb = externalRatings?.imdb ?: RatingDetails.Unavailable

    return copy(
      ratings = ratings +
        (RatingSource.METACRITIC to metascore) +
        (RatingSource.RT to rt) +
        (RatingSource.IMDB to imdb),
    )
  }
}
