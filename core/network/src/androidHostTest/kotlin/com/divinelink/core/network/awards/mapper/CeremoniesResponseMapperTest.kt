package com.divinelink.core.network.awards.mapper

import com.divinelink.core.network.awards.model.ceremony.CeremoniesResponse
import com.divinelink.core.testing.factories.api.awards.CeremonyResponseFactory
import com.divinelink.core.fixtures.model.awards.CeremonyFactory
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class CeremoniesResponseMapperTest {

  @Test
  fun `test map CeremoniesResponse to list of Ceremony`() {
    val response = CeremonyResponseFactory.all()

    val mapped = response.map()

    mapped shouldBe CeremonyFactory.all()
  }

  @Test
  fun `test map empty CeremoniesResponse`() {
    val response = CeremoniesResponse(ceremonies = emptyList())

    val mapped = response.map()

    mapped shouldBe emptyList()
  }
}
