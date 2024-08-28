package com.divinelink.core.network.changes.model.serializer

import com.divinelink.core.network.changes.model.api.ChangeItemApi
import com.divinelink.core.network.client.localJson
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class ChangeApiSerializerTest {

  // TODO : Add tests for all deserialize cases

//  @Test
//  fun `test deserialize changes json response`() = JvmUnitTestDemoAssetManager
//    .open("changes.json")
//    .use {
//      val changes = it.readBytes().decodeToString()
//
//      val serializer: KSerializer<ChangeApi> = ChangeApiSerializer
//      val result = localJson.decodeFromString(ChangeApiSerializer, changes)
//
//      assertThat(result.key).isEqualTo("person:4495")
//    }

  @Test
  fun `test deserialize biography`() {
    val biography = """
    {
       "key": "biography",
       "items": [
        {
          "id": "640469b113654500ba4e859a",
          "action": "added",
          "time": "2023-03-05 10:06:41 UTC",
          "iso_639_1": "ca",
          "iso_3166_1": "ES",
          "value": "Thomas \"Tom\" Jeffrey Hanks (Concord, Califòrnia, 9 de juliol de 1956)"
        }
      ]
    }
    """.trimIndent()

    val result = localJson.decodeFromString(ChangeApiSerializer, biography)

    assertThat(result.key).isEqualTo("biography")

    assertThat(result.items.size).isEqualTo(1)
    assertThat(result.items.first()).isEqualTo(
      ChangeItemApi(
        id = "640469b113654500ba4e859a",
        action = "added",
        time = "2023-03-05 10:06:41 UTC",
        value = StringValue(
          "Thomas \"Tom\" Jeffrey Hanks (Concord, Califòrnia, 9 de juliol de 1956)",
        ),
        iso6391 = "ca",
        iso31661 = "ES",
        originalValue = null,
      ),
    )
  }

  @Test
  fun `test deserialize translations`() {
    val translation = """
    {
       "key": "translations",
       "items": [
        {
          "id": "640469b113654500ba4e859a",
          "action": "added",
          "time": "2023-03-05 10:06:41 UTC",
          "iso_639_1": "ca",
          "iso_3166_1": "ES",
          "value": "Thomas \"Tom\" Jeffrey Hanks (Concord, Califòrnia, 9 de juliol de 1956)"
        }
      ]
    }
    """.trimIndent()

    val result = localJson.decodeFromString(ChangeApiSerializer, translation)

    assertThat(result.key).isEqualTo("translations")

    assertThat(result.items.size).isEqualTo(1)
    assertThat(result.items.first()).isEqualTo(
      ChangeItemApi(
        id = "640469b113654500ba4e859a",
        action = "added",
        time = "2023-03-05 10:06:41 UTC",
        value = StringValue(
          "Thomas \"Tom\" Jeffrey Hanks (Concord, Califòrnia, 9 de juliol de 1956)",
        ),
        iso6391 = "ca",
        iso31661 = "ES",
        originalValue = null,
      ),
    )
  }
}
