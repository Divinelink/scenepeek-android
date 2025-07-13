package com.divinelink.core.network.list.mapper.add

import com.divinelink.core.model.list.AddToListResult
import com.divinelink.core.network.client.localJson
import com.divinelink.core.network.list.model.add.AddToListResponse
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class AddToListResponseMapperTest {

  @Test
  fun `test AddToListResponse mapping with item already exists error`() {
    val response = """
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
    """
      .trimIndent()

    val addToListResponse = localJson.decodeFromString(AddToListResponse.serializer(), response)

    val mapped = addToListResponse.map()

    assertThat(mapped).isEqualTo(
      AddToListResult.Failure.ItemAlreadyExists,
    )
  }

  @Test
  fun `test AddToListResponse mapping with success`() {
    val response = """
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
    """.trimIndent()

    val addToListResponse = localJson.decodeFromString(AddToListResponse.serializer(), response)

    val mapped = addToListResponse.map()

    assertThat(mapped).isEqualTo(AddToListResult.Success)
  }
}
