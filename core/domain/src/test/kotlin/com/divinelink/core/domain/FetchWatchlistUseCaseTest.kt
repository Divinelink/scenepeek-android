package com.divinelink.core.domain

import com.divinelink.core.data.session.model.SessionException
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.factories.storage.SessionStorageFactory
import com.divinelink.core.testing.repository.TestAccountRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class FetchWatchlistUseCaseTest {

  private lateinit var sessionStorage: SessionStorage
  private val accountRepository: TestAccountRepository = TestAccountRepository()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  @Test
  fun `given null accountId when fetching movies watchlist then expect exception`() = runTest {
    sessionStorage = SessionStorageFactory.noAccountId()

    val useCase = FetchWatchlistUseCase(
      dispatcher = testDispatcher,
      sessionStorage = sessionStorage,
      accountRepository = accountRepository.mock
    )

    val result = useCase.invoke(
      parameters = WatchlistParameters(
        page = 1,
        mediaType = MediaType.MOVIE
      )
    ).last()

    assertThat(result.isFailure).isTrue()
    assertThat(result.exceptionOrNull()).isInstanceOf(SessionException.Unauthenticated::class.java)
  }

  @Test
  fun `given null sessionId when fetching movies watchlist then expect exception`() = runTest {
    sessionStorage = SessionStorageFactory.noSessionId()

    val useCase = FetchWatchlistUseCase(
      dispatcher = testDispatcher,
      sessionStorage = sessionStorage,
      accountRepository = accountRepository.mock
    )

    val result = useCase.invoke(
      parameters = WatchlistParameters(
        page = 1,
        mediaType = MediaType.MOVIE
      )
    ).last()

    assertThat(result.isFailure).isTrue()
    assertThat(result.exceptionOrNull()).isInstanceOf(SessionException.Unauthenticated::class.java)
  }
}
