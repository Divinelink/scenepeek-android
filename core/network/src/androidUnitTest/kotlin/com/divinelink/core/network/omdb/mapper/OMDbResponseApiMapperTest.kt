package com.divinelink.core.network.omdb.mapper

import com.divinelink.core.model.details.rating.ExternalRatings
import com.divinelink.core.model.details.rating.RatingDetails
import com.divinelink.core.network.omdb.model.OMDbResponseApi
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class OMDbResponseApiMapperTest {

  @Test
  fun `test OMDbResponseApi mapper with invalid rating`() {
    val api = OMDbResponseApi(
      metascore = "N/A",
      imdbRating = "N/A",
      imdbVotes = "51,000",
      ratings = emptyList(),
    )

    val mapped = api.map()

    mapped shouldBe ExternalRatings(
      imdb = RatingDetails.Unavailable,
      rt = RatingDetails.Unavailable,
      metascore = RatingDetails.Unavailable,
    )
  }

  @Test
  fun `test OMDbResponseApi mapper with invalid voteCount`() {
    val api = OMDbResponseApi(
      metascore = "N/A",
      imdbRating = "8.8",
      imdbVotes = "N/A",
      ratings = emptyList(),
    )

    val mapped = api.map()

    mapped shouldBe ExternalRatings(
      imdb = RatingDetails.Unavailable,
      rt = RatingDetails.Unavailable,
      metascore = RatingDetails.Unavailable,
    )
  }

  @Test
  fun `test OMDbResponseApi mapper with valid data`() {
    val api = OMDbResponseApi(
      metascore = "N/A",
      imdbRating = "8.8",
      imdbVotes = "51,000",
      ratings = emptyList(),
    )

    val mapped = api.map()

    mapped shouldBe ExternalRatings(
      imdb = RatingDetails.Score(
        voteAverage = 8.8,
        voteCount = 51000,
      ),
      rt = RatingDetails.Unavailable,
      metascore = RatingDetails.Unavailable,
    )
  }

  @Test
  fun `test OMDbResponseApi mapper with external ratings`() {
    val api = OMDbResponseApi(
      metascore = "N/A",
      imdbRating = "8.8",
      imdbVotes = "51,000",
      ratings = listOf(
        OMDbResponseApi.OMDbRatingSourceResponse(
          source = "Rotten Tomatoes",
          value = "75%",
        ),
        OMDbResponseApi.OMDbRatingSourceResponse(
          source = "Metacritic",
          value = "46/100",
        ),
      ),
    )

    val mapped = api.map()

    mapped shouldBe ExternalRatings(
      imdb = RatingDetails.Score(
        voteAverage = 8.8,
        voteCount = 51000,
      ),
      rt = RatingDetails.Rating(75),
      metascore = RatingDetails.Rating(46),
    )
  }
}
