package com.divinelink.core.domain.jellyseerr

import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.repository.TestJellyseerrRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test

class DeleteMediaUseCaseTest {

  private val repository = TestJellyseerrRepository()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  @Test
  fun `test delete media and file with success`() = runTest {
    repository.mockDeleteFile(Result.success(Unit))
    repository.mockDeleteMedia(Result.success(Unit))

    val useCase = DeleteMediaUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    ).invoke(
      DeleteMediaParameters(
        mediaId = 1,
        deleteFile = true,
      ),
    )

    assertThat(useCase.isSuccess).isTrue()
  }

  @Test
  fun `test delete file with failure also deletes media`() = runTest {
    repository.mockDeleteFile(Result.failure(RuntimeException()))
    repository.mockDeleteMedia(Result.success(Unit))

    val useCase = DeleteMediaUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    ).invoke(
      DeleteMediaParameters(
        mediaId = 1,
        deleteFile = true,
      ),
    )

    assertThat(useCase.isSuccess).isTrue()
  }

  @Test
  fun `test delete file and media with failure`() = runTest {
    repository.mockDeleteFile(Result.failure(Exception("Failed to delete file")))
    repository.mockDeleteMedia(Result.failure(Exception("Failed to delete media")))

    val result = DeleteMediaUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    ).invoke(
      DeleteMediaParameters(
        mediaId = 1,
        deleteFile = true,
      ),
    )

    assertThat(result).isInstanceOf(
      Result.failure<Exception>(Exception("Failed to delete media"))::class.java,
    )
  }

  @Test
  fun `test delete media only with failure`() = runTest {
    repository.mockDeleteMedia(Result.failure(Exception("Failed to delete media")))

    val result = DeleteMediaUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    ).invoke(
      DeleteMediaParameters(
        mediaId = 1,
        deleteFile = false,
      ),
    )

    assertThat(result).isInstanceOf(
      Result.failure<Exception>(Exception("Failed to delete media"))::class.java,
    )
  }

  @Test
  fun `test delete media only with success`() = runTest {
    repository.mockDeleteMedia(Result.success(Unit))

    val result = DeleteMediaUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    ).invoke(
      DeleteMediaParameters(
        mediaId = 1,
        deleteFile = false,
      ),
    )

    assertThat(result.isSuccess).isTrue()
  }
}
