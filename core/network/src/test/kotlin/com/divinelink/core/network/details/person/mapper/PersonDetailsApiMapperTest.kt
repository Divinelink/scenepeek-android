package com.divinelink.core.network.details.person.mapper

import JvmUnitTestDemoAssetManager
import com.divinelink.core.data.person.details.mapper.map
import com.divinelink.core.data.person.details.mapper.mapToEntity
import com.divinelink.core.network.client.localJson
import com.divinelink.core.network.details.person.model.PersonDetailsApi
import com.divinelink.core.testing.factories.details.person.PersonDetailsFactory
import com.divinelink.core.testing.factories.entity.person.PersonEntityFactory
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class PersonDetailsApiMapperTest {

  @Test
  fun `test person details mapper`() = JvmUnitTestDemoAssetManager.open("person-details.json").use {
    val personDetails = it.readBytes().decodeToString().trimIndent()

    val serializer = PersonDetailsApi.serializer()
    val personDetailsApi = localJson.decodeFromString(serializer, personDetails)
    val mappedPersonDetails = personDetailsApi.map()
    val mappedToEntity = personDetailsApi.mapToEntity("1628995200")

    assertThat(mappedPersonDetails).isEqualTo(PersonDetailsFactory.steveCarell())
    assertThat(mappedToEntity).isEqualTo(PersonEntityFactory.steveCarell())
  }

  @Test
  fun `test person details entity mapper`() = JvmUnitTestDemoAssetManager
    .open("person-details.json")
    .use {
      val personDetails = it.readBytes().decodeToString().trimIndent()

      val serializer = PersonDetailsApi.serializer()
      val personDetailsApi = localJson.decodeFromString(serializer, personDetails)

      val mappedToEntity = personDetailsApi.mapToEntity("1628995200")

      assertThat(mappedToEntity).isEqualTo(PersonEntityFactory.steveCarell())
    }
}
