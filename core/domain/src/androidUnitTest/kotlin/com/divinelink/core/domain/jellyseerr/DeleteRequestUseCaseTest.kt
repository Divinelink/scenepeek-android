package com.divinelink.core.domain.jellyseerr

import app.cash.turbine.test
import com.divinelink.core.fixtures.model.jellyseerr.media.JellyseerrMediaInfoFactory
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.factories.api.media.MediaRequestApiFactory
import com.divinelink.core.testing.repository.TestJellyseerrRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test

class DeleteRequestUseCaseTest {

  private val repository = TestJellyseerrRepository()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  @Test
  fun `test delete request with success also fetches movie details`() = runTest {
    val useCase = DeleteRequestUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    repository.mockDeleteRequest(Result.success(Unit))
    repository.mockGetMovieDetails(JellyseerrMediaInfoFactory.Movie.available())

    val params = DeleteRequestParameters(
      requestId = 1,
      mediaRequest = MediaRequestApiFactory.movie(),
    )

    useCase.invoke(params).test {
      assertThat(awaitItem()).isEqualTo(
        Result.success(JellyseerrMediaInfoFactory.Movie.available()),
      )

      awaitComplete()
    }
  }

  @Test
  fun `test delete request with null fetch movie details`() = runTest {
    val useCase = DeleteRequestUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    repository.mockDeleteRequest(Result.success(Unit))
    repository.mockGetMovieDetails(null)

    val params = DeleteRequestParameters(
      requestId = 1,
      mediaRequest = MediaRequestApiFactory.movie(),
    )

    useCase.invoke(params).test {
      assertThat(awaitItem()).isInstanceOf(
        Result.failure<Exception>(Exception("Movie details not found"))::class.java,
      )

      awaitComplete()
    }
  }

  @Test
  fun `test delete tv request with success also fetches tv details`() = runTest {
    val useCase = DeleteRequestUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    repository.mockDeleteRequest(Result.success(Unit))
    repository.mockGetTvDetails(JellyseerrMediaInfoFactory.Tv.partiallyAvailable())

    val params = DeleteRequestParameters(
      requestId = 1,
      mediaRequest = MediaRequestApiFactory.tv(),
    )

    useCase.invoke(params).test {
      assertThat(awaitItem()).isEqualTo(
        Result.success(JellyseerrMediaInfoFactory.Tv.partiallyAvailable()),
      )

      awaitComplete()
    }
  }

  @Test
  fun `test delete tv request with null fetch tv details`() = runTest {
    val useCase = DeleteRequestUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    repository.mockDeleteRequest(Result.success(Unit))
    repository.mockGetTvDetails(null)

    val params = DeleteRequestParameters(
      requestId = 1,
      mediaRequest = MediaRequestApiFactory.tv(),
    )

    useCase.invoke(params).test {
      assertThat(awaitItem()).isInstanceOf(
        Result.failure<Exception>(Exception("TV details not found"))::class.java,
      )

      awaitComplete()
    }
  }

  @Test
  fun `test delete request with failure`() = runTest {
    val useCase = DeleteRequestUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    repository.mockDeleteRequest(Result.failure(Exception("Failed to delete request")))

    val params = DeleteRequestParameters(
      requestId = 1,
      mediaRequest = MediaRequestApiFactory.movie(),
    )

    useCase.invoke(params).test {
      assertThat(awaitItem()).isInstanceOf(
        Result.failure<Exception>(Exception("Failed to delete request"))::class.java,
      )
      awaitComplete()
    }
  }
}
