package com.divinelink.core.network.list.service

import com.divinelink.core.network.list.model.AddToListRequest
import com.divinelink.core.network.list.model.AddToListResponse
import com.divinelink.core.network.list.model.MediaItemRequest
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
      mediaId = 67890,
      mediaType = "movie",
    )

    assertThat(result.isSuccess).isTrue()
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
      mediaId = 67890,
      mediaType = "movie",
    )

    assertThat(result.isSuccess).isTrue()
  }
}
