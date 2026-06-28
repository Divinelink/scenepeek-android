package com.divinelink.core.network.awards.mapper

import com.divinelink.core.fixtures.model.awards.CeremonyCategoryFactory
import com.divinelink.core.network.awards.model.category.CeremonyCategoriesResponse
import com.divinelink.core.testing.factories.api.awards.CeremonyCategoriesResponseFactory
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class CeremonyCategoriesMapperTest {

  @Test
  fun `test map CeremonyCategoriesResponse to list of CeremonyCategory`() {
    val response = CeremonyCategoriesResponseFactory.all()

    val mapped = response.map()

    mapped shouldBe CeremonyCategoryFactory.all()
  }

  @Test
  fun `test map empty CeremonyCategoriesResponse`() {
    val response = CeremonyCategoriesResponse(categories = emptyList())

    val mapped = response.map()

    mapped shouldBe emptyList()
  }
}
