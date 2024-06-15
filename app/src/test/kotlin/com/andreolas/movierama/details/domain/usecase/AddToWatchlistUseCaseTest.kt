package com.andreolas.movierama.details.domain.usecase

import com.andreolas.movierama.MainDispatcherRule
import com.andreolas.movierama.fakes.repository.FakeDetailsRepository
import com.andreolas.movierama.test.util.fakes.FakeEncryptedPreferenceStorage
import com.andreolas.movierama.test.util.fakes.FakePreferenceStorage
import com.divinelink.core.data.session.model.SessionException
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.model.media.MediaType
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddToWatchlistUseCaseTest {

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
  fun `test user with no account id cannot add to watchlist`() = runTest {
    sessionStorage = createSessionStorage(accountId = null)

    val useCase = com.divinelink.feature.details.usecase.AddToWatchlistUseCase(
      sessionStorage = sessionStorage,
      repository = repository.mock,
      dispatcher = testDispatcher
    )

    val result = useCase.invoke(
      com.divinelink.feature.details.usecase.AddToWatchlistParameters(
        id = 0,
        mediaType = MediaType.MOVIE,
        addToWatchlist = true
      )
    )

    assertThat(result.first().isFailure).isTrue()
    assertThat(
      result.first().exceptionOrNull()
    ).isInstanceOf(SessionException.InvalidAccountId::class.java)
  }

  @Test
  fun `test user with account id can add to watchlist for movie`() = runTest {
    sessionStorage = createSessionStorage(accountId = "123")

    repository.mockAddToWatchlist(
      response = Result.success(Unit)
    )

    val useCase = com.divinelink.feature.details.usecase.AddToWatchlistUseCase(
      sessionStorage = sessionStorage,
      repository = repository.mock,
      dispatcher = testDispatcher
    )

    val result = useCase.invoke(
      com.divinelink.feature.details.usecase.AddToWatchlistParameters(
        id = 0,
        mediaType = MediaType.MOVIE,
        addToWatchlist = true
      )
    )

    assertThat(result.first().isSuccess).isTrue()
  }

  @Test
  fun `test user with account id can add to watchlist for tv show`() = runTest {
    sessionStorage = createSessionStorage(accountId = "123")

    val useCase = com.divinelink.feature.details.usecase.AddToWatchlistUseCase(
      sessionStorage = sessionStorage,
      repository = repository.mock,
      dispatcher = testDispatcher
    )

    repository.mockAddToWatchlist(
      response = Result.success(Unit)
    )

    val result = useCase.invoke(
      com.divinelink.feature.details.usecase.AddToWatchlistParameters(
        id = 0,
        mediaType = MediaType.TV,
        addToWatchlist = true
      )
    )

    assertThat(result.first().isSuccess).isTrue()
  }

  @Test
  fun `test invalid media type throws exception`() = runTest {
    sessionStorage = createSessionStorage(accountId = "123")

    val useCase = com.divinelink.feature.details.usecase.AddToWatchlistUseCase(
      sessionStorage = sessionStorage,
      repository = repository.mock,
      dispatcher = testDispatcher
    )

    val result = useCase.invoke(
      com.divinelink.feature.details.usecase.AddToWatchlistParameters(
        id = 0,
        mediaType = MediaType.MOVIE,
        addToWatchlist = false
      )
    )

    assertThat(result.first().isFailure).isTrue()
    assertThat(result.first().exceptionOrNull()).isInstanceOf(Exception::class.java)
  }

  private fun createSessionStorage(accountId: String?) =
    SessionStorage(
      storage = FakePreferenceStorage(accountId = accountId),
      encryptedStorage = FakeEncryptedPreferenceStorage()
    )
}
