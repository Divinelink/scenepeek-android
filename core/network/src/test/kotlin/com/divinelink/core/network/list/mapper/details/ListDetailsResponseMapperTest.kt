package com.divinelink.core.network.list.mapper.details

import JvmUnitTestDemoAssetManager
import com.divinelink.core.fixtures.model.list.ListDetailsFactory
import com.divinelink.core.network.client.localJson
import com.divinelink.core.network.list.model.details.ListDetailsResponse
import kotlin.test.Test
import kotlin.test.assertEquals

class ListDetailsResponseMapperTest {

  @Test
  fun `test ListDetailsResponse mapping`() = JvmUnitTestDemoAssetManager
    .open("list-details.json")
    .use {
      val responseJson = it.readBytes().decodeToString().trimIndent()
      val listDetailsResponse = localJson.decodeFromString(
        ListDetailsResponse.serializer(),
        responseJson,
      )

      val mapped = listDetailsResponse.map()

      assertEquals(mapped.media.size, 3)
      assertEquals(
        mapped,
        ListDetailsFactory.mustWatch(),
      )
    }
}
