package com.divinelink.core.data.list

import com.divinelink.core.commons.domain.data
import com.divinelink.core.model.list.AddToListResult
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.client.localJson
import com.divinelink.core.network.list.model.add.AddToListResponse
import com.divinelink.core.testing.service.TestListService
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test

class ProdListRepositoryTest {

  private lateinit var repository: ListRepository

  private val service = TestListService()

  @Before
  fun setUp() {
    repository = ProdListRepository(
      service = service.mock,
    )
  }

  @Test
  fun `test add items to list when item already exists`() = runTest {
    val listId = 123

    val response = """
        {
          "success": true,
          "status_code": 1,
          "status_message": "Success.",
          "results": [
            {
              "media_id": 4108,
              "media_type": "tv",
              "error": [
                "Media has already been taken"
              ],
              "success": false
            }
          ]
        }
    """
      .trimIndent()

    val addToListResponse = localJson.decodeFromString(AddToListResponse.serializer(), response)

    service.mockAddItemToList(
      Result.success(addToListResponse),
    )

    val result = repository.addItemToList(
      listId = listId,
      mediaId = 4108,
      mediaType = MediaType.TV.name,
    )

    assertThat(
      result.data,
    ).isEqualTo(
      AddToListResult.Failure.ItemAlreadyExists,
    )
  }

  @Test
  fun `test add items to list with success`() = runTest {
    val listId = 123

    val response = """
       {
         "success": true,
         "status_code": 1,
         "status_message": "Success.",
         "results": [
         {
           "media_id": 4108,
           "media_type": "tv",
           "success": true
         }
         ]
       }
    """.trimIndent()

    val addToListResponse = localJson.decodeFromString(AddToListResponse.serializer(), response)

    service.mockAddItemToList(
      Result.success(addToListResponse),
    )

    val result = repository.addItemToList(
      listId = listId,
      mediaId = 4108,
      mediaType = MediaType.TV.name,
    )

    assertThat(
      result.data,
    ).isEqualTo(
      AddToListResult.Success,
    )
  }
}
