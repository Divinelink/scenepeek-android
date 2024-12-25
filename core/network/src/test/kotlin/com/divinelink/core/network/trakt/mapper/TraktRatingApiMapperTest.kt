package com.divinelink.core.network.trakt.mapper

import com.divinelink.core.model.details.rating.RatingDetails
import com.divinelink.core.network.trakt.model.TraktRatingApi
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class TraktRatingApiMapperTest {

  @Test
  fun `test TraktRatingApiMapper`() {
    val traktRatingApi = TraktRatingApi(
      rating = 8.8,
      votes = 1_234,
    )

    val mapped = traktRatingApi.map()

    assertThat(mapped).isEqualTo(
      RatingDetails.Score(
        voteAverage = 8.8,
        voteCount = 1_234,
      ),
    )
  }
}
