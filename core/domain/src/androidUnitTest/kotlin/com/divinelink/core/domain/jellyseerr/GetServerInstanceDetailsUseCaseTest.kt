package com.divinelink.core.domain.jellyseerr

import com.divinelink.core.fixtures.model.jellyseerr.server.radarr.RadarrInstanceDetailsFactory
import com.divinelink.core.fixtures.model.jellyseerr.server.radarr.RadarrInstanceFactory
import com.divinelink.core.fixtures.model.jellyseerr.server.sonarr.SonarrInstanceDetailsFactory
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

class GetServerInstanceDetailsUseCaseTest {

  private val repository = TestJellyseerrRepository()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  @Test
  fun `test getServerInstances for tv with success`() = runTest {
    repository.mockGetSonarrDetails(response = Result.success(SonarrInstanceDetailsFactory.sonarr))

    val useCase = GetServerInstanceDetailsUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    ).invoke(
      parameters = GetServerInstanceDetailsUseCase.Parameters(
        mediaType = MediaType.TV,
        serverId = 0,
      ),
    )

    useCase.getOrThrow() shouldBe SonarrInstanceDetailsFactory.sonarr
  }

  @Test
  fun `test getServerInstances for tv with failure`() = runTest {
    repository.mockGetSonarrDetails(response = Result.failure(AppException.Unknown()))

    val useCase = GetServerInstanceDetailsUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    ).invoke(
      parameters = GetServerInstanceDetailsUseCase.Parameters(
        mediaType = MediaType.TV,
        serverId = 0,
      ),
    )

    shouldThrow<AppException.Unknown> {
      useCase.getOrThrow()
    }
  }

  @Test
  fun `test getServerInstances for movie with success`() = runTest {
    repository.mockGetRadarrDetails(response = Result.success(RadarrInstanceDetailsFactory.radarr))

    val useCase = GetServerInstanceDetailsUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    ).invoke(
      parameters = GetServerInstanceDetailsUseCase.Parameters(
        mediaType = MediaType.MOVIE,
        serverId = 0,
      ),
    )

    useCase.getOrThrow() shouldBe RadarrInstanceDetailsFactory.radarr
  }

  @Test
  fun `test getServerInstances for movie with failure`() = runTest {
    repository.mockGetRadarrDetails(response = Result.failure(AppException.Unknown()))

    val useCase = GetServerInstanceDetailsUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    ).invoke(
      parameters = GetServerInstanceDetailsUseCase.Parameters(
        mediaType = MediaType.MOVIE,
        serverId = 0,
      ),
    )

    shouldThrow<AppException.Unknown> {
      useCase.getOrThrow()
    }
  }

  @Test
  fun `test getServerInstances for other media type`() = runTest {
    val useCase = GetServerInstanceDetailsUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    ).invoke(
      parameters = GetServerInstanceDetailsUseCase.Parameters(
        mediaType = MediaType.PERSON,
        serverId = 0,
      ),
    )

    shouldThrow<IllegalStateException> {
      useCase.getOrThrow()
    }
  }
}
