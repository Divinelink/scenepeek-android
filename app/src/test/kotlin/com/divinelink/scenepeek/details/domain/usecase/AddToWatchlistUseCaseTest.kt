package com.divinelink.scenepeek.details.domain.usecase

import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.fixtures.model.account.AccountDetailsFactory
import com.divinelink.core.fixtures.model.session.AccessTokenFactory
import com.divinelink.core.model.exception.SessionException
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.session.AccessToken
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.repository.TestDetailsRepository
import com.divinelink.core.testing.storage.FakeAccountStorage
import com.divinelink.core.testing.storage.FakeEncryptedPreferenceStorage
import com.divinelink.core.testing.storage.FakePreferenceStorage
import com.divinelink.feature.details.media.usecase.AddToWatchlistParameters
import com.divinelink.feature.details.media.usecase.AddToWatchlistUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test

class AddToWatchlistUseCaseTest {

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
  fun `test user with no account id cannot add to watchlist`() = runTest {
    sessionStorage = createSessionStorage(accountId = null, sessionId = "123")

    val useCase = AddToWatchlistUseCase(
      sessionStorage = sessionStorage,
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    val result = useCase.invoke(
      AddToWatchlistParameters(
        id = 0,
        mediaType = MediaType.MOVIE,
        addToWatchlist = true,
      ),
    )

    assertThat(result.isFailure).isTrue()
    assertThat(result.exceptionOrNull()).isInstanceOf(SessionException.Unauthenticated::class.java)
  }

  @Test
  fun `test user with no session id cannot add to watchlist`() = runTest {
    sessionStorage = createSessionStorage(sessionId = null, accountId = "123")

    val useCase = AddToWatchlistUseCase(
      sessionStorage = sessionStorage,
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    val result = useCase.invoke(
      AddToWatchlistParameters(
        id = 0,
        mediaType = MediaType.MOVIE,
        addToWatchlist = true,
      ),
    )

    assertThat(result.isFailure).isTrue()
    assertThat(result.exceptionOrNull()).isInstanceOf(SessionException.Unauthenticated::class.java)
  }

  @Test
  fun `test user with account and session id can add to watchlist for movie`() = runTest {
    sessionStorage = createSessionStorage(accountId = "123", sessionId = "123")

    repository.mockAddToWatchlist(
      response = Result.success(Unit),
    )

    val useCase = AddToWatchlistUseCase(
      sessionStorage = sessionStorage,
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    val result = useCase.invoke(
      AddToWatchlistParameters(
        id = 0,
        mediaType = MediaType.MOVIE,
        addToWatchlist = true,
      ),
    )

    assertThat(result.isSuccess).isTrue()
  }

  @Test
  fun `test user with account and session id can add to watchlist for tv show`() = runTest {
    sessionStorage = createSessionStorage(
      accountId = "123",
      sessionId = "123",
      accessToken = AccessTokenFactory.valid(),
    )

    val useCase = AddToWatchlistUseCase(
      sessionStorage = sessionStorage,
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    repository.mockAddToWatchlist(
      response = Result.success(Unit),
    )

    val result = useCase.invoke(
      AddToWatchlistParameters(
        id = 0,
        mediaType = MediaType.TV,
        addToWatchlist = true,
      ),
    )

    assertThat(result.isSuccess).isTrue()
  }

  @Test
  fun `test invalid media type throws exception`() = runTest {
    sessionStorage = createSessionStorage(accountId = "123", sessionId = "123")

    val useCase = AddToWatchlistUseCase(
      sessionStorage = sessionStorage,
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    val result = useCase.invoke(
      AddToWatchlistParameters(
        id = 0,
        mediaType = MediaType.UNKNOWN,
        addToWatchlist = false,
      ),
    )

    assertThat(result.isFailure).isTrue()
    assertThat(result.exceptionOrNull()).isInstanceOf(Exception::class.java)
  }

  private fun createSessionStorage(
    accountId: String?,
    sessionId: String?,
    accessToken: AccessToken? = null,
  ) = SessionStorage(
    storage = FakePreferenceStorage(),
    encryptedStorage = FakeEncryptedPreferenceStorage(
      sessionId = sessionId,
      accessToken = accessToken?.accessToken,
      tmdbAccountId = accountId,
    ),
    accountStorage = FakeAccountStorage(
      accountDetails = accountId?.let {
        AccountDetailsFactory.Pinkman().copy(id = it.toInt())
      },
    ),
  )
}
