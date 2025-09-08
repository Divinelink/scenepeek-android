package com.divinelink.core.domain.jellyseerr

import com.divinelink.core.fixtures.model.jellyseerr.server.radarr.RadarrInstanceFactory
import com.divinelink.core.fixtures.model.jellyseerr.server.sonarr.SonarrInstanceFactory
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.repository.TestJellyseerrRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test

class GetServerInstancesUseCaseTest {

  private val repository = TestJellyseerrRepository()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  @Test
  fun `test getServerInstances for tv with success`() = runTest {
    repository.mockGetSonarrInstances(response = Result.success(SonarrInstanceFactory.all))

    val useCase = GetServerInstancesUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    ).invoke(
      parameters = MediaType.TV,
    )

    useCase.getOrThrow() shouldBe SonarrInstanceFactory.all
  }

  @Test
  fun `test getServerInstances for tv with failure`() = runTest {
    repository.mockGetSonarrInstances(response = Result.failure(AppException.Unknown()))

    val useCase = GetServerInstancesUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    ).invoke(
      parameters = MediaType.TV,
    )

    shouldThrow<AppException.Unknown> {
      useCase.getOrThrow()
    }
  }

  @Test
  fun `test getServerInstances for movie with success`() = runTest {
    repository.mockGetRadarrInstances(response = Result.success(RadarrInstanceFactory.all))

    val useCase = GetServerInstancesUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    ).invoke(
      parameters = MediaType.MOVIE,
    )

    useCase.getOrThrow() shouldBe RadarrInstanceFactory.all
  }

  @Test
  fun `test getServerInstances for movie with failure`() = runTest {
    repository.mockGetRadarrInstances(response = Result.failure(AppException.Unknown()))

    val useCase = GetServerInstancesUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    ).invoke(
      parameters = MediaType.MOVIE,
    )

    shouldThrow<AppException.Unknown> {
      useCase.getOrThrow()
    }
  }

  @Test
  fun `test getServerInstances for other media type`() = runTest {
    val useCase = GetServerInstancesUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    ).invoke(
      parameters = MediaType.PERSON,
    )

    shouldThrow<IllegalStateException> {
      useCase.getOrThrow()
    }
  }
}
