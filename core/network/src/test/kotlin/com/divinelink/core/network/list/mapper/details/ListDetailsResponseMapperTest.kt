package com.divinelink.core.network.list.mapper.details

import JvmUnitTestDemoAssetManager
import com.divinelink.core.fixtures.model.list.ListDetailsFactory
import com.divinelink.core.network.client.localJson
import com.divinelink.core.network.list.model.details.ListDetailsResponse
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class ListDetailsResponseMapperTest {

  @Test
  fun `test ListDetailsResponse mapping`() {
    JvmUnitTestDemoAssetManager
      .open("list-details.json")
      .use {
        val responseJson = it.readBytes().decodeToString().trimIndent()
        val listDetailsResponse = localJson.decodeFromString(
          ListDetailsResponse.serializer(),
          responseJson,
        )

        val mapped = listDetailsResponse.map()

        mapped.media.size shouldBe 3
        mapped shouldBe ListDetailsFactory.mustWatch()
      }
  }
}
