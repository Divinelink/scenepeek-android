package com.divinelink.core.network.trakt.service

import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.trakt.model.TraktRatingApi
import com.divinelink.core.testing.network.TestTraktClient
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class ProdTraktServiceTest {

  private lateinit var service: ProdTraktService
  private lateinit var testClient: TestTraktClient

  @BeforeTest
  fun setUp() {
    testClient = TestTraktClient()
  }

  @Test
  fun `test fetch imdb details with valid ratings`() = runTest {
    testClient.mockGetResponse<TraktRatingApi>(
      url = "https://api.trakt.tv/movies/tt0290978/ratings",
      json = """
        {
          "rating": 8,
          "votes": 4271,
          "distribution": {
            "1": 88,
            "2": 30,
            "3": 35,
            "4": 53,
            "5": 112,
            "6": 273,
            "7": 691,
            "8": 1160,
            "9": 896,
            "10": 931
          }
        }
      """.trimIndent(),
    )

    service = ProdTraktService(testClient.client)

    val response = service.fetchRating(
      mediaType = MediaType.MOVIE,
      imdbId = "tt0290978",
    ).single()

    assertThat(response).isEqualTo(
      TraktRatingApi(
        rating = 8.0,
        votes = 4271,
      ),
    )
  }
}
