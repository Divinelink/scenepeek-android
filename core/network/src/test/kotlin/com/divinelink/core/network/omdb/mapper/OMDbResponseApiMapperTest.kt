package com.divinelink.core.network.omdb.mapper

import com.divinelink.core.model.details.rating.RatingDetails
import com.divinelink.core.network.omdb.model.OMDbResponseApi
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class OMDbResponseApiMapperTest {

  @Test
  fun `test OMDbResponseApi mapper with invalid rating`() {
    val api = OMDbResponseApi(
      metascore = "N/A",
      imdbRating = "N/A",
      imdbVotes = "51,000",
    )

    val mapped = api.map()

    assertThat(mapped).isEqualTo(RatingDetails.Unavailable)
  }

  @Test
  fun `test OMDbResponseApi mapper with invalid voteCount`() {
    val api = OMDbResponseApi(
      metascore = "N/A",
      imdbRating = "8.8",
      imdbVotes = "N/A",
    )

    val mapped = api.map()

    assertThat(mapped).isEqualTo(RatingDetails.Unavailable)
  }

  @Test
  fun `test OMDbResponseApi mapper with valid data`() {
    val api = OMDbResponseApi(
      metascore = "N/A",
      imdbRating = "8.8",
      imdbVotes = "51,000",
    )

    val mapped = api.map()

    assertThat(mapped).isEqualTo(
      RatingDetails.Score(
        voteAverage = 8.8,
        voteCount = 51000,
      ),
    )
  }
}
