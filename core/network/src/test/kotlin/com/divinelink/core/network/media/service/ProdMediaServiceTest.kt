package com.divinelink.core.network.media.service

import app.cash.turbine.test
import com.divinelink.core.network.media.model.rating.DeleteRatingRequestApi
import com.divinelink.core.testing.network.TestRestClient
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class ProdMediaServiceTest {

  private lateinit var service: ProdMediaService
  private lateinit var testRestClient: TestRestClient

  @BeforeTest
  fun setUp() {
    testRestClient = TestRestClient()
  }

  @Test
  fun `test delete submit rating with success`() = runTest {
    testRestClient.mockDelete<Unit>(
      url = "https://api.themoviedb.org/3/movie/574475/rating?session_id=9b256",
      response = """
        {
          "success": true,
          "status_code": 13,
          "status_message": "The item/record was deleted successfully."
        }
      """.trimIndent(),
    )

    service = ProdMediaService(testRestClient.restClient)

    service.deleteRating(
      DeleteRatingRequestApi.Movie(
        movieId = 574475,
        sessionId = "9b256",
      ),
    ).test {
      assertThat(awaitItem()).isEqualTo(Unit)
      awaitComplete()
    }
  }
}
