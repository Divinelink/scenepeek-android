package com.divinelink.core.network.awards.mapper

import com.divinelink.core.testing.factories.api.awards.AwardsResponseFactory
import com.divinelink.core.fixtures.model.awards.YearAwardsFactory
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class AwardsResponseMapperTest {

  @Test
  fun `test map AwardsResponse with movies`() {
    val response = AwardsResponseFactory.withMovies()

    val mapped = response.map()

    mapped shouldBe listOf(YearAwardsFactory.withMovies())
  }

  @Test
  fun `test map AwardsResponse with shows`() {
    val response = AwardsResponseFactory.withShows()

    val mapped = response.map()

    mapped shouldBe listOf(YearAwardsFactory.withShows())
  }

  @Test
  fun `test map AwardsResponse with persons`() {
    val response = AwardsResponseFactory.withPersons()

    val mapped = response.map()

    mapped shouldBe listOf(YearAwardsFactory.withPersons())
  }

  @Test
  fun `test map AwardsResponse with no nominees returns empty list`() {
    val response = AwardsResponseFactory.empty()

    val mapped = response.map()

    mapped shouldBe listOf(YearAwardsFactory.empty())
  }
}
