package com.andreolas.movierama.details.domain.usecase

import com.andreolas.movierama.MainDispatcherRule
import com.andreolas.movierama.fakes.repository.FakeDetailsRepository
import com.andreolas.movierama.test.util.fakes.FakeEncryptedPreferenceStorage
import com.andreolas.movierama.test.util.fakes.FakePreferenceStorage
import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.session.model.SessionException
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.model.media.MediaType
import com.divinelink.feature.details.usecase.DeleteRatingParameters
import com.divinelink.feature.details.usecase.DeleteRatingUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DeleteRatingUseCaseTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private lateinit var repository: FakeDetailsRepository

  private lateinit var sessionStorage: SessionStorage

  @Before
  fun setUp() {
    repository = FakeDetailsRepository()
  }

  @Test
  fun `test user with no session id cannot submit rating`() = runTest {
    sessionStorage = createSessionStorage(sessionId = null)

    val useCase = DeleteRatingUseCase(
      sessionStorage = sessionStorage,
      repository = repository.mock,
      dispatcher = testDispatcher
    )

    val result = useCase.invoke(
      DeleteRatingParameters(
        id = 0,
        mediaType = MediaType.MOVIE,
      )
    )

    assertThat(result.first().isFailure).isTrue()
    assertThat(
      result.first().exceptionOrNull()
    ).isInstanceOf(SessionException.Unauthenticated::class.java)
  }

  @Test
  fun `test user with session id can delete rating for movie`() = runTest {
    sessionStorage = createSessionStorage(sessionId = "session_id")

    repository.mockDeleteRating(
      response = Result.success(Unit)
    )

    val useCase = DeleteRatingUseCase(
      sessionStorage = sessionStorage,
      repository = repository.mock,
      dispatcher = testDispatcher
    )

    val result = useCase.invoke(
      DeleteRatingParameters(
        id = 0,
        mediaType = MediaType.MOVIE,
      )
    )

    assertThat(result.first().isSuccess).isTrue()
    assertThat(result.first().data).isEqualTo(Unit)
  }

  @Test
  fun `test user with session id can delete rating for tv`() = runTest {
    sessionStorage = createSessionStorage(sessionId = "session_id")

    repository.mockDeleteRating(
      response = Result.success(Unit)
    )

    val useCase = DeleteRatingUseCase(
      sessionStorage = sessionStorage,
      repository = repository.mock,
      dispatcher = testDispatcher
    )

    val result = useCase.invoke(
      DeleteRatingParameters(
        id = 0,
        mediaType = MediaType.TV,
      )
    )

    assertThat(result.first().isSuccess).isTrue()
    assertThat(result.first().data).isEqualTo(Unit)
  }

  @Test
  fun `test cannot fetch account media details for unknown media type`() = runTest {
    sessionStorage = createSessionStorage(sessionId = "session_id")

    val useCase = DeleteRatingUseCase(
      sessionStorage = sessionStorage,
      repository = repository.mock,
      dispatcher = testDispatcher
    )

    val result = useCase.invoke(
      DeleteRatingParameters(
        id = 0,
        mediaType = MediaType.UNKNOWN,
      )
    )

    assertThat(result.first().isFailure).isTrue()
    assertThat(result.first().exceptionOrNull()).isInstanceOf(Exception::class.java)
  }

  private fun createSessionStorage(sessionId: String?) =
    SessionStorage(
      storage = FakePreferenceStorage(),
      encryptedStorage = FakeEncryptedPreferenceStorage(sessionId = sessionId)
    )
}