package com.divinelink.core.network.media.service

import com.divinelink.core.network.media.model.details.watchlist.AddToWatchlistRequestApi
import com.divinelink.core.network.media.model.details.watchlist.SubmitOnAccountResponse
import com.divinelink.core.network.media.model.rating.AddRatingRequestApi
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

    val result = service.deleteRating(
      DeleteRatingRequestApi.Movie(
        movieId = 574475,
        sessionId = "9b256",
      ),
    )

    assertThat(result).isEqualTo(
      Result.success(
        SubmitOnAccountResponse(
          success = true,
          statusCode = 13,
          statusMessage = "The item/record was deleted successfully.",
        ),
      ),
    )
  }

  @Test
  fun `test delete submit rating with multiple failures`() = runTest {
    testRestClient.mockDelete<Unit>(
      url = "https://api.themoviedb.org/3/movie/574475/rating?session_id=9b256",
      response = """
        {
          "success": false,
          "status_code": 34,
          "status_message": "Resource could not be found."
        }
      """.trimIndent(),
    )

    service = ProdMediaService(testRestClient.restClient)

    val result = service.deleteRating(
      DeleteRatingRequestApi.Movie(
        movieId = 574475,
        sessionId = "9b256",
      ),
    )

    assertThat(result.toString()).isEqualTo(
      Result.failure<Exception>(
        Exception("Resource not found after 10 attempts: Resource could not be found."),
      ).toString(),
    )
  }

  @Test
  fun `test submit submit rating with multiple failures`() = runTest {
    testRestClient.mockDelete<Unit>(
      url = "https://api.themoviedb.org/3/movie/574475/rating?session_id=9b256",
      response = """
        {
          "success": false,
          "status_code": 34,
          "status_message": "Resource could not be found."
        }
      """.trimIndent(),
    )

    service = ProdMediaService(testRestClient.restClient)

    val result = service.submitRating(
      AddRatingRequestApi.Movie(
        movieId = 574475,
        sessionId = "9b256",
        rating = 8,
      ),
    )

    assertThat(result.toString()).isEqualTo(
      Result.failure<Exception>(
        Exception("Resource not found after 10 attempts: Resource could not be found."),
      ).toString(),
    )
  }

  @Test
  fun `test submit submit rating with success`() = runTest {
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

    val result = service.submitRating(
      AddRatingRequestApi.Movie(
        movieId = 574475,
        sessionId = "9b256",
        rating = 8,
      ),
    )

    assertThat(result).isEqualTo(
      Result.success(
        SubmitOnAccountResponse(
          success = true,
          statusCode = 13,
          statusMessage = "The item/record was deleted successfully.",
        ),
      ),
    )
  }

  @Test
  fun `test add to watchlist with success`() = runTest {
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

    val result = service.addToWatchlist(
      AddToWatchlistRequestApi.Movie(
        movieId = 574475,
        sessionId = "9b256",
        accountId = "12345",
        addToWatchlist = true,
      ),
    )

    assertThat(result).isEqualTo(
      Result.success(
        SubmitOnAccountResponse(
          success = true,
          statusCode = 13,
          statusMessage = "The item/record was deleted successfully.",
        ),
      ),
    )
  }
}
