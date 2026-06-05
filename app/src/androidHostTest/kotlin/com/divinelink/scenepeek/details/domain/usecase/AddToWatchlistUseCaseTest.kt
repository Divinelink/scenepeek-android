package com.divinelink.scenepeek.details.domain.usecase

import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.domain.details.media.AddToAccountUseCase
import com.divinelink.core.domain.details.media.AddToWatchlistParameters
import com.divinelink.core.model.details.AccountDataSection
import com.divinelink.core.model.exception.SessionException
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.factories.storage.SessionStorageFactory
import com.divinelink.core.testing.repository.TestDetailsRepository
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
    sessionStorage = SessionStorageFactory.empty()

    val useCase = AddToAccountUseCase(
      sessionStorage = sessionStorage,
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    val result = useCase.invoke(
      AddToWatchlistParameters(
        id = 0,
        mediaType = MediaType.MOVIE,
        section = AccountDataSection.Watchlist,
        shouldAdd = true,
      ),
    )

    assertThat(result.isFailure).isTrue()
    assertThat(result.exceptionOrNull()).isInstanceOf(SessionException.Unauthenticated::class.java)
  }

  @Test
  fun `test user with no session id cannot add to watchlist`() = runTest {
    sessionStorage = SessionStorageFactory.empty()

    val useCase = AddToAccountUseCase(
      sessionStorage = sessionStorage,
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    val result = useCase.invoke(
      AddToWatchlistParameters(
        id = 0,
        mediaType = MediaType.MOVIE,
        section = AccountDataSection.Watchlist,
        shouldAdd = true,
      ),
    )

    assertThat(result.isFailure).isTrue()
    assertThat(result.exceptionOrNull()).isInstanceOf(SessionException.Unauthenticated::class.java)
  }

  @Test
  fun `test user with account and session id can add to watchlist for movie`() = runTest {
    sessionStorage = SessionStorageFactory.full()

    repository.mockAddToWatchlist(
      response = Result.success(Unit),
    )

    val useCase = AddToAccountUseCase(
      sessionStorage = sessionStorage,
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    val result = useCase.invoke(
      AddToWatchlistParameters(
        id = 0,
        mediaType = MediaType.MOVIE,
        section = AccountDataSection.Watchlist,
        shouldAdd = true,
      ),
    )

    assertThat(result.isSuccess).isTrue()
  }

  @Test
  fun `test user with account and session id can add to watchlist for tv show`() = runTest {
    sessionStorage = SessionStorageFactory.full()

    val useCase = AddToAccountUseCase(
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
        section = AccountDataSection.Watchlist,
        shouldAdd = true,
      ),
    )

    assertThat(result.isSuccess).isTrue()
  }

  @Test
  fun `test user with account and session id can mark as favorite for tv show`() = runTest {
    sessionStorage = SessionStorageFactory.full()

    val useCase = AddToAccountUseCase(
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
        section = AccountDataSection.Favorite,
        shouldAdd = true,
      ),
    )

    assertThat(result.isSuccess).isTrue()
  }

  @Test
  fun `test invalid media type throws exception`() = runTest {
    sessionStorage = SessionStorageFactory.full()

    val useCase = AddToAccountUseCase(
      sessionStorage = sessionStorage,
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    val result = useCase.invoke(
      AddToWatchlistParameters(
        id = 0,
        mediaType = MediaType.UNKNOWN,
        section = AccountDataSection.Watchlist,
        shouldAdd = false,
      ),
    )

    assertThat(result.isFailure).isTrue()
    assertThat(result.exceptionOrNull()).isInstanceOf(Exception::class.java)
  }
}
