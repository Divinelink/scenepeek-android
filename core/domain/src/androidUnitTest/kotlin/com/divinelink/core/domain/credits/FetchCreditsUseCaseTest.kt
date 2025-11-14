package com.divinelink.core.domain.credits

import app.cash.turbine.test
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.factories.details.credits.AggregatedCreditsFactory
import com.divinelink.core.testing.repository.TestDetailsRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test

class FetchCreditsUseCaseTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private var repository: TestDetailsRepository = TestDetailsRepository()

  @Test
  fun `test fetch credits use case`() = runTest {
    repository.mockFetchAggregateCredits(
      response = Result.success(
        AggregatedCreditsFactory.credits(),
      ),
    )
    val useCase = FetchCreditsUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    useCase(AggregatedCreditsFactory.credits().id).test {
      awaitItem().let { result ->
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(AggregatedCreditsFactory.credits())
      }
      awaitComplete()
    }
  }

  @Test
  fun `test multiple emissions from use case`() = runTest {
    val castChunked = AggregatedCreditsFactory.credits().cast.chunked(3)
    val crew = AggregatedCreditsFactory.credits().crewDepartments

    repository.mockFetchAggregateCredits(
      response = flowOf(
        Result.success(
          AggregatedCreditsFactory.credits().copy(
            cast = castChunked[0],
            crewDepartments = crew,
          ),
        ),
        Result.success(
          AggregatedCreditsFactory.credits().copy(
            cast = castChunked[0] + castChunked[1],
            crewDepartments = crew,
          ),
        ),
        Result.success(
          AggregatedCreditsFactory.credits().copy(
            cast = castChunked[0] + castChunked[1] + castChunked[2],
            crewDepartments = crew,
          ),
        ),
      ),
    )

    val useCase = FetchCreditsUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    useCase(AggregatedCreditsFactory.credits().id).test {
      awaitItem().let { result ->
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(
          AggregatedCreditsFactory.credits().copy(
            cast = castChunked[0],
            crewDepartments = crew,
          ),
        )
      }
      awaitItem().let { result ->
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(
          AggregatedCreditsFactory.credits().copy(
            cast = castChunked[0] + castChunked[1],
            crewDepartments = crew,
          ),
        )
      }
      awaitItem().let { result ->
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(
          AggregatedCreditsFactory.credits().copy(
            cast = castChunked[0] + castChunked[1] + castChunked[2],
            crewDepartments = crew,
          ),
        )
      }
      awaitComplete()
    }
  }
}
