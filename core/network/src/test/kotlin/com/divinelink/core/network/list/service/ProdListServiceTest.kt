package com.divinelink.core.network.list.service

import com.divinelink.core.model.media.MediaReference
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.list.model.MediaItemRequest
import com.divinelink.core.network.list.model.add.AddToListRequest
import com.divinelink.core.network.list.model.add.AddToListResponse
import com.divinelink.core.network.list.model.details.ListDetailsResponse
import com.divinelink.core.testing.network.TestAuthTMDbClient
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class ProdListServiceTest {

  private lateinit var service: ProdListService
  private lateinit var testRestClient: TestAuthTMDbClient

  @BeforeTest
  fun setUp() {
    testRestClient = TestAuthTMDbClient()
  }

  @Test
  fun `test add item to list when item already exists`() = runTest {
    testRestClient.mockPost<AddToListRequest, AddToListResponse>(
      url = "https://api.themoviedb.org/4/list/12345/items",
      response = """
        {
          "success": true,
          "status_code": 1,
          "status_message": "Success.",
          "results": [
            {
              "media_id": 749170,
              "media_type": "movie",
              "error": [
                "Media has already been taken"
              ],
              "success": false
            }
          ]
        }
      """.trimIndent(),
      body = AddToListRequest(
        items = listOf(
          MediaItemRequest(
            mediaId = 67890,
            mediaType = "movie",
          ),
        ),
      ),
    )

    service = ProdListService(testRestClient.restClient)

    val result = service.addItemToList(
      listId = 12345,
      media = MediaReference(
        mediaId = 67890,
        mediaType = MediaType.MOVIE,
      ),

    )

    assertThat(result.isSuccess).isTrue()
  }

  @Test
  fun `test add item to list with media is required error makes retry attemps`() = runTest {
    testRestClient.mockPost<AddToListRequest, AddToListResponse>(
      url = "https://api.themoviedb.org/4/list/12345/items",
      response = """
        {
          "success": true,
          "status_code": 1,
          "status_message": "Success.",
          "results": [
            {
              "media_id": 749170,
              "media_type": "movie",
              "error": [
                "Media is required"
              ],
              "success": false
            }
          ]
        }
      """.trimIndent(),
      body = AddToListRequest(
        items = listOf(
          MediaItemRequest(
            mediaId = 67890,
            mediaType = "movie",
          ),
        ),
      ),
    )

    service = ProdListService(testRestClient.restClient)

    val result = service.addItemToList(
      listId = 12345,
      media = MediaReference(
        mediaId = 67890,
        mediaType = MediaType.MOVIE,
      ),

    )

    assertThat(result.toString()).isEqualTo(
      Result.failure<Exception>(Exception("Resource not found after 10 attempts")).toString(),
    )
  }

  @Test
  fun `test add item to list with success`() = runTest {
    testRestClient.mockPost<AddToListRequest, AddToListResponse>(
      url = "https://api.themoviedb.org/4/list/12345/items",
      response = """
       {
         "success": true,
         "status_code": 1,
         "status_message": "Success.",
         "results": [
         {
           "media_id": 15600,
           "media_type": "movie",
           "success": true
         }
         ]
       }
      """.trimIndent(),
      body = AddToListRequest(
        items = listOf(
          MediaItemRequest(
            mediaId = 67890,
            mediaType = "movie",
          ),
        ),
      ),
    )

    service = ProdListService(testRestClient.restClient)

    val result = service.addItemToList(
      listId = 12345,
      MediaReference(
        mediaId = 67890,
        mediaType = MediaType.MOVIE,
      ),
    )

    assertThat(result.isSuccess).isTrue()
  }

  @Test
  fun `test fetch list details with success`() = runTest {
    testRestClient.mockGet<ListDetailsResponse>(
      url = "https://api.themoviedb.org/4/list/12345",
      jsonFileName = "list-details.json",
    )

    service = ProdListService(testRestClient.restClient)

    val result = service.fetchListDetails(
      listId = 12345,
      page = 1,
    )

    assertThat(result.isSuccess).isTrue()
  }
}
