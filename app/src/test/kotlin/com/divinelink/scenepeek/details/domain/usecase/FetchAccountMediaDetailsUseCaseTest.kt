package com.divinelink.scenepeek.details.domain.usecase

import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.details.model.MediaDetailsParams
import com.divinelink.core.data.session.model.SessionException
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.repository.TestDetailsRepository
import com.divinelink.core.testing.storage.FakeAccountStorage
import com.divinelink.core.testing.storage.FakeEncryptedPreferenceStorage
import com.divinelink.core.testing.storage.FakePreferenceStorage
import com.divinelink.factories.details.domain.model.account.AccountMediaDetailsFactory
import com.divinelink.feature.details.media.usecase.FetchAccountMediaDetailsUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test

class FetchAccountMediaDetailsUseCaseTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private lateinit var repository: TestDetailsRepository

  private lateinit var sessionStorage: SessionStorage

  @Before
  fun setUp() {
    repository = TestDetailsRepository()
  }

  @Test
  fun `test user with no session id cannot fetch account media details`() = runTest {
    sessionStorage = createSessionStorage(sessionId = null)

    val useCase = FetchAccountMediaDetailsUseCase(
      sessionStorage = sessionStorage,
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    val result = useCase.invoke(
      MediaDetailsParams(
        id = 0,
        mediaType = MediaType.MOVIE,
      ),
    )

    assertThat(result.first().isFailure).isTrue()
    assertThat(
      result.first().exceptionOrNull(),
    ).isInstanceOf(SessionException.Unauthenticated::class.java)
  }

  @Test
  fun `test user with session id can fetch account media details for movie`() = runTest {
    sessionStorage = createSessionStorage(sessionId = "session_id")

    repository.mockFetchAccountMediaDetails(
      response = Result.success(AccountMediaDetailsFactory.Rated()),
    )

    val useCase = FetchAccountMediaDetailsUseCase(
      sessionStorage = sessionStorage,
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    val result = useCase.invoke(
      MediaDetailsParams(
        id = 0,
        mediaType = MediaType.MOVIE,
      ),
    )

    assertThat(result.first().isSuccess).isTrue()
    assertThat(result.first().data).isEqualTo(AccountMediaDetailsFactory.Rated())
  }

  @Test
  fun `test user with session id can fetch account media details for tv show`() = runTest {
    sessionStorage = createSessionStorage(sessionId = "session_id")

    repository.mockFetchAccountMediaDetails(
      response = Result.success(AccountMediaDetailsFactory.Rated()),
    )

    val useCase = FetchAccountMediaDetailsUseCase(
      sessionStorage = sessionStorage,
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    val result = useCase.invoke(
      MediaDetailsParams(
        id = 0,
        mediaType = MediaType.TV,
      ),
    )

    assertThat(result.first().isSuccess).isTrue()
    assertThat(result.first().data).isEqualTo(AccountMediaDetailsFactory.Rated())
  }

  @Test
  fun `test cannot fetch account media details for unknown media type`() = runTest {
    sessionStorage = createSessionStorage(sessionId = "session_id")

    val useCase = FetchAccountMediaDetailsUseCase(
      sessionStorage = sessionStorage,
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    val result = useCase.invoke(
      MediaDetailsParams(
        id = 0,
        mediaType = MediaType.UNKNOWN,
      ),
    )

    assertThat(result.first().isFailure).isTrue()
    assertThat(result.first().exceptionOrNull()).isInstanceOf(Exception::class.java)
  }

  private fun createSessionStorage(sessionId: String?) = SessionStorage(
    storage = FakePreferenceStorage(),
    encryptedStorage = FakeEncryptedPreferenceStorage(sessionId = sessionId),
    accountStorage = FakeAccountStorage(),
  )
}
