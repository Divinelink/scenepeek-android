package com.divinelink.core.network.details.person.mapper

import JvmUnitTestDemoAssetManager
import com.divinelink.core.data.person.credits.mapper.toEntityCast
import com.divinelink.core.data.person.credits.mapper.toEntityCrew
import com.divinelink.core.data.person.details.mapper.mapToEntity
import com.divinelink.core.network.client.localJson
import com.divinelink.core.network.details.person.model.PersonCreditsApi
import com.divinelink.core.network.details.person.model.PersonDetailsApi
import com.divinelink.core.testing.factories.entity.person.PersonEntityFactory
import com.divinelink.core.testing.factories.entity.person.credits.PersonCastCreditEntityFactory
import com.divinelink.core.testing.factories.entity.person.credits.PersonCrewCreditEntityFactory
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class PersonCreditsApiMapperTest {

  @Test
  fun `test person credits mapper`() = JvmUnitTestDemoAssetManager
    .open("person-combined-credits.json")
    .use {
      val personDetails = it.readBytes().decodeToString().trimIndent()

      val serializer = PersonCreditsApi.serializer()
      val personCreditsApi = localJson.decodeFromString(serializer, personDetails)

      val mappedCastCredits = personCreditsApi.toEntityCast()
      val mappedCrewCredits = personCreditsApi.toEntityCrew()

      assertThat(mappedCastCredits.subList(0, 4)).isEqualTo(PersonCastCreditEntityFactory.all())
      assertThat(mappedCrewCredits.subList(0, 4)).isEqualTo(PersonCrewCreditEntityFactory.all())
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
