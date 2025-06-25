package com.divinelink.core.domain.jellyseerr

import com.divinelink.core.data.jellyseerr.model.JellyseerrRequestParams
import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.fixtures.model.jellyseerr.media.JellyseerrRequestFactory
import com.divinelink.core.fixtures.model.jellyseerr.request.JellyseerrMediaRequestResponseFactory
import com.divinelink.core.model.exception.MissingJellyseerrHostAddressException
import com.divinelink.core.model.jellyseerr.media.JellyseerrStatus
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.repository.TestJellyseerrRepository
import com.divinelink.core.testing.storage.FakePreferenceStorage
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test

class RequestMediaUseCaseTest {

  private lateinit var preferenceStorage: PreferenceStorage

  private val repository = TestJellyseerrRepository()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  @Test
  fun `test requestMedia with null address throws exception`() = runTest {
    preferenceStorage = FakePreferenceStorage()

    val useCase = RequestMediaUseCase(
      repository = repository.mock,
      storage = preferenceStorage,
      dispatcher = testDispatcher,
    )

    val params = JellyseerrRequestParams(
      mediaType = MediaType.MOVIE.name,
      mediaId = 123,
      is4k = false,
      seasons = emptyList(),
    )

    useCase.invoke(params).collect {
      assertThat(it.isFailure).isTrue()
      assertThat(it.exceptionOrNull()).isInstanceOf(
        MissingJellyseerrHostAddressException::class.java,
      )
      assertThat(it.exceptionOrNull()?.message).isEqualTo("Jellyseerr host address is not set.")
    }
  }

  @Test
  fun `test requestMedia with success and success request details response`() = runTest {
    preferenceStorage = FakePreferenceStorage(jellyseerrAddress = "http://localhost:8096")

    repository.mockRequestMedia(Result.success(JellyseerrMediaRequestResponseFactory.movie()))
    repository.mockRequestDetails(Result.success(JellyseerrRequestFactory.movie()))

    val useCase = RequestMediaUseCase(
      repository = repository.mock,
      storage = preferenceStorage,
      dispatcher = testDispatcher,
    )

    val params = JellyseerrRequestParams(
      mediaType = MediaType.MOVIE.name,
      mediaId = 123,
      is4k = false,
      seasons = emptyList(),
    )

    useCase.invoke(params).collect {
      assertThat(it.isSuccess).isTrue()
      assertThat(it.getOrNull()).isEqualTo(
        JellyseerrMediaRequestResponseFactory.movieWithRequest(),
      )
    }
  }

  @Test
  fun `test tv series requestMedia  with success and success request details response`() = runTest {
    preferenceStorage = FakePreferenceStorage(jellyseerrAddress = "http://localhost:8096")

    repository.mockRequestMedia(Result.success(JellyseerrMediaRequestResponseFactory.tvPartially()))
    repository.mockRequestDetails(Result.success(JellyseerrRequestFactory.Tv.betterCallSaul2()))

    val useCase = RequestMediaUseCase(
      repository = repository.mock,
      storage = preferenceStorage,
      dispatcher = testDispatcher,
    )

    val params = JellyseerrRequestParams(
      mediaType = MediaType.TV.name,
      mediaId = 123,
      is4k = false,
      seasons = emptyList(),
    )

    val response = JellyseerrMediaRequestResponseFactory.tvPartially().copy(
      mediaInfo = JellyseerrMediaRequestResponseFactory.tvPartially().mediaInfo.copy(
        requests = JellyseerrRequestFactory.Tv.all(),
        status = JellyseerrStatus.Media.PENDING,
      ),
    )

    useCase.invoke(params).collect {
      assertThat(it.isSuccess).isTrue()
      assertThat(it.getOrNull()).isEqualTo(response)
    }
  }

  @Test
  fun `test requestMedia with success and success request with null details response`() = runTest {
    preferenceStorage = FakePreferenceStorage(jellyseerrAddress = "http://localhost:8096")

    repository.mockRequestMedia(Result.success(JellyseerrMediaRequestResponseFactory.movie()))
    repository.mockRequestDetails(Result.failure(Exception("Request details not found")))

    val useCase = RequestMediaUseCase(
      repository = repository.mock,
      storage = preferenceStorage,
      dispatcher = testDispatcher,
    )

    val params = JellyseerrRequestParams(
      mediaType = MediaType.MOVIE.name,
      mediaId = 123,
      is4k = false,
      seasons = emptyList(),
    )

    useCase.invoke(params).collect {
      assertThat(it.isSuccess).isTrue()
      assertThat(it.getOrNull()).isEqualTo(
        JellyseerrMediaRequestResponseFactory.movie(),
      )
    }
  }

  @Test
  fun `test requestMedia with failure`() = runTest {
    preferenceStorage = FakePreferenceStorage(jellyseerrAddress = "http://localhost:8096")

    repository.mockRequestMedia(Result.failure(IllegalArgumentException("Something went wrong")))

    val useCase = RequestMediaUseCase(
      repository = repository.mock,
      storage = preferenceStorage,
      dispatcher = testDispatcher,
    )

    val params = JellyseerrRequestParams(
      mediaType = MediaType.MOVIE.name,
      mediaId = 123,
      is4k = false,
      seasons = emptyList(),
    )

    useCase.invoke(params).collect {
      assertThat(it.isFailure).isTrue()
      assertThat(it.exceptionOrNull()).isInstanceOf(IllegalArgumentException::class.java)
      assertThat(it.exceptionOrNull()?.message).isEqualTo("Something went wrong")
    }
  }
}
