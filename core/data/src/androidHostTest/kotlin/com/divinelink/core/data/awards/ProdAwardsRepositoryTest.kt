package com.divinelink.core.data.awards

import com.divinelink.core.testing.factories.api.awards.AwardsResponseFactory
import com.divinelink.core.testing.factories.api.awards.CeremonyCategoriesResponseFactory
import com.divinelink.core.testing.factories.api.awards.CeremonyResponseFactory
import com.divinelink.core.testing.factories.model.awards.CeremonyCategoryFactory
import com.divinelink.core.testing.factories.model.awards.CeremonyFactory
import com.divinelink.core.testing.factories.model.awards.YearAwardsFactory
import com.divinelink.core.testing.service.TestAwardsService
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class ProdAwardsRepositoryTest {

  private lateinit var repository: AwardsRepository
  private val service = TestAwardsService()

  @BeforeTest
  fun setUp() {
    repository = ProdAwardsRepository(service = service.mock)
  }

  @Test
  fun `test fetchAwardCeremonies with success`() = runTest {
    service.mockFetchAwardCeremonies(Result.success(CeremonyResponseFactory.all()))

    val result = repository.fetchAwardCeremonies()

    result shouldBe Result.success(CeremonyFactory.all())
  }

  @Test
  fun `test fetchAwardCeremonies with failure`() = runTest {
    val error = RuntimeException("Network error")
    service.mockFetchAwardCeremonies(Result.failure(error))

    val result = repository.fetchAwardCeremonies()

    result.isFailure shouldBe true
    result.exceptionOrNull()!!.message shouldBe "Network error"
  }

  @Test
  fun `test fetchCeremonyCategories with success`() = runTest {
    service.mockFetchCeremonyCategories(Result.success(CeremonyCategoriesResponseFactory.all()))

    val result = repository.fetchCeremonyCategories(id = "oscars")

    result shouldBe Result.success(CeremonyCategoryFactory.all())
  }

  @Test
  fun `test fetchCeremonyCategories with failure`() = runTest {
    val error = RuntimeException("Network error")
    service.mockFetchCeremonyCategories(Result.failure(error))

    val result = repository.fetchCeremonyCategories(id = "oscars")

    result.isFailure shouldBe true
    result.exceptionOrNull()!!.message shouldBe "Network error"
  }

  @Test
  fun `test fetchAwards with movies success`() = runTest {
    service.mockFetchAwards(Result.success(AwardsResponseFactory.withMovies()))

    val result = repository.fetchAwards(ceremonyId = "oscars", categoryId = "best-picture")

    result shouldBe Result.success(listOf(YearAwardsFactory.withMovies()))
  }

  @Test
  fun `test fetchAwards with shows success`() = runTest {
    service.mockFetchAwards(Result.success(AwardsResponseFactory.withShows()))

    val result = repository.fetchAwards(ceremonyId = "emmys", categoryId = "best-drama")

    result shouldBe Result.success(listOf(YearAwardsFactory.withShows()))
  }

  @Test
  fun `test fetchAwards with persons success`() = runTest {
    service.mockFetchAwards(Result.success(AwardsResponseFactory.withPersons()))

    val result = repository.fetchAwards(ceremonyId = "oscars", categoryId = "best-actor")

    result shouldBe Result.success(listOf(YearAwardsFactory.withPersons()))
  }

  @Test
  fun `test fetchAwards with failure`() = runTest {
    val error = RuntimeException("Network error")
    service.mockFetchAwards(Result.failure(error))

    val result = repository.fetchAwards(ceremonyId = "oscars", categoryId = "best-picture")

    result.isFailure shouldBe true
    result.exceptionOrNull()!!.message shouldBe "Network error"
  }
}
