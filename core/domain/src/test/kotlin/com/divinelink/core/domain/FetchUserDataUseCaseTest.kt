package com.divinelink.core.domain

import app.cash.turbine.test
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.fixtures.model.account.AccountDetailsFactory
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.fixtures.model.session.AccessTokenFactory
import com.divinelink.core.model.exception.SessionException
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.user.data.UserDataParameters
import com.divinelink.core.model.user.data.UserDataResponse
import com.divinelink.core.model.user.data.UserDataSection
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.factories.storage.SessionStorageFactory
import com.divinelink.core.testing.repository.TestAccountRepository
import com.divinelink.core.testing.storage.FakeAccountStorage
import com.divinelink.core.testing.storage.FakeEncryptedPreferenceStorage
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test

class FetchUserDataUseCaseTest {

  private lateinit var sessionStorage: SessionStorage
  private val accountRepository: TestAccountRepository = TestAccountRepository()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  @Test
  fun `given null accountId when fetching movies watchlist then expect exception`() = runTest {
    sessionStorage = SessionStorageFactory.noAccountId()

    val useCase = FetchUserDataUseCase(
      dispatcher = testDispatcher,
      sessionStorage = sessionStorage,
      accountRepository = accountRepository.mock,
    )

    val result = useCase.invoke(
      parameters = UserDataParameters(
        page = 1,
        mediaType = MediaType.MOVIE,
        section = UserDataSection.Watchlist,
      ),
    ).last()

    assertThat(result.isFailure).isTrue()
    assertThat(result.exceptionOrNull()).isInstanceOf(SessionException.Unauthenticated::class.java)
  }

  @Test
  fun `given null sessionId when fetching movies watchlist then expect exception`() = runTest {
    sessionStorage = SessionStorageFactory.noSessionId()

    val useCase = FetchUserDataUseCase(
      dispatcher = testDispatcher,
      sessionStorage = sessionStorage,
      accountRepository = accountRepository.mock,
    )

    val result = useCase.invoke(
      parameters = UserDataParameters(
        page = 1,
        mediaType = MediaType.MOVIE,
        section = UserDataSection.Watchlist,
      ),
    ).last()

    assertThat(result.isFailure).isTrue()
    assertThat(result.exceptionOrNull()).isInstanceOf(SessionException.Unauthenticated::class.java)
  }

  @Test
  fun `test fetch tv watchlist with success`() = runTest {
    sessionStorage = SessionStorage(
      encryptedStorage = FakeEncryptedPreferenceStorage(
        sessionId = "123456789",
        tmdbAccountId = AccessTokenFactory.valid().accountId,
      ),
      accountStorage = FakeAccountStorage(
        accountDetails = AccountDetailsFactory.Pinkman().copy(id = 123456789),
      ),
    )

    accountRepository.mockFetchTvShowsWatchlist(
      flowOf(Result.success(MediaItemFactory.tvPagination())),
    )

    val useCase = FetchUserDataUseCase(
      dispatcher = testDispatcher,
      sessionStorage = sessionStorage,
      accountRepository = accountRepository.mock,
    )

    useCase.invoke(
      parameters = UserDataParameters(
        page = 1,
        mediaType = MediaType.TV,
        section = UserDataSection.Watchlist,
      ),
    ).test {
      assertThat(awaitItem()).isEqualTo(
        Result.success(
          UserDataResponse(
            data = MediaItemFactory.tvPagination().list,
            totalResults = MediaItemFactory.tvPagination().totalResults,
            type = MediaType.TV,
            canFetchMore = true,
            page = 1,
          ),
        ),
      )
      awaitComplete()
    }
  }

  @Test
  fun `test fetch movies watchlist with success`() = runTest {
    sessionStorage = SessionStorage(
      encryptedStorage = FakeEncryptedPreferenceStorage(
        sessionId = "123456789",
        tmdbAccountId = AccessTokenFactory.valid().accountId,
      ),
      accountStorage = FakeAccountStorage(
        accountDetails = AccountDetailsFactory.Pinkman().copy(id = 123456789),
      ),
    )

    accountRepository.mockFetchMoviesWatchlist(
      flowOf(Result.success(MediaItemFactory.moviesPagination())),
    )

    val useCase = FetchUserDataUseCase(
      dispatcher = testDispatcher,
      sessionStorage = sessionStorage,
      accountRepository = accountRepository.mock,
    )

    useCase.invoke(
      parameters = UserDataParameters(
        page = 1,
        mediaType = MediaType.MOVIE,
        section = UserDataSection.Watchlist,
      ),
    ).test {
      assertThat(awaitItem()).isEqualTo(
        Result.success(
          UserDataResponse(
            data = MediaItemFactory.moviesPagination().list,
            totalResults = MediaItemFactory.moviesPagination().totalResults,
            type = MediaType.MOVIE,
            canFetchMore = true,
            page = 1,
          ),
        ),
      )
      awaitComplete()
    }
  }

  @Test
  fun `test fetch rated tv with success`() = runTest {
    sessionStorage = SessionStorage(
      encryptedStorage = FakeEncryptedPreferenceStorage(
        sessionId = "123456789",
        tmdbAccountId = AccessTokenFactory.valid().accountId,
      ),
      accountStorage = FakeAccountStorage(
        accountDetails = AccountDetailsFactory.Pinkman().copy(id = 123456789),
      ),
    )

    accountRepository.mockFetchRatedTvShows(
      flowOf(Result.success(MediaItemFactory.tvPagination())),
    )

    val useCase = FetchUserDataUseCase(
      dispatcher = testDispatcher,
      sessionStorage = sessionStorage,
      accountRepository = accountRepository.mock,
    )

    useCase.invoke(
      parameters = UserDataParameters(
        page = 1,
        mediaType = MediaType.TV,
        section = UserDataSection.Ratings,
      ),
    ).test {
      assertThat(awaitItem()).isEqualTo(
        Result.success(
          UserDataResponse(
            data = MediaItemFactory.tvPagination().list,
            totalResults = MediaItemFactory.tvPagination().totalResults,
            type = MediaType.TV,
            canFetchMore = true,
            page = 1,
          ),
        ),
      )
      awaitComplete()
    }
  }

  @Test
  fun `test fetch rated movies with success`() = runTest {
    sessionStorage = SessionStorage(
      encryptedStorage = FakeEncryptedPreferenceStorage(
        sessionId = "123456789",
        tmdbAccountId = AccessTokenFactory.valid().accountId,
      ),
      accountStorage = FakeAccountStorage(
        accountDetails = AccountDetailsFactory.Pinkman().copy(id = 123456789),
      ),
    )

    accountRepository.mockFetchRatedMovies(
      flowOf(Result.success(MediaItemFactory.moviesPagination())),
    )

    val useCase = FetchUserDataUseCase(
      dispatcher = testDispatcher,
      sessionStorage = sessionStorage,
      accountRepository = accountRepository.mock,
    )

    useCase.invoke(
      parameters = UserDataParameters(
        page = 1,
        mediaType = MediaType.MOVIE,
        section = UserDataSection.Ratings,
      ),
    ).test {
      assertThat(awaitItem()).isEqualTo(
        Result.success(
          UserDataResponse(
            data = MediaItemFactory.moviesPagination().list,
            totalResults = MediaItemFactory.moviesPagination().totalResults,
            type = MediaType.MOVIE,
            canFetchMore = true,
            page = 1,
          ),
        ),
      )
      awaitComplete()
    }
  }

  @Test
  fun `test canFetchMore is false when page is greater than total pages`() = runTest {
    sessionStorage = SessionStorage(
      encryptedStorage = FakeEncryptedPreferenceStorage(
        sessionId = "123456789",
        tmdbAccountId = AccessTokenFactory.valid().accountId,
      ),
      accountStorage = FakeAccountStorage(
        accountDetails = AccountDetailsFactory.Pinkman().copy(id = 123456789),
      ),
    )

    accountRepository.mockFetchRatedMovies(
      flowOf(Result.success(MediaItemFactory.moviesPagination())),
    )

    val useCase = FetchUserDataUseCase(
      dispatcher = testDispatcher,
      sessionStorage = sessionStorage,
      accountRepository = accountRepository.mock,
    )

    useCase.invoke(
      parameters = UserDataParameters(
        page = 3,
        mediaType = MediaType.MOVIE,
        section = UserDataSection.Ratings,
      ),
    ).test {
      assertThat(awaitItem()).isEqualTo(
        Result.success(
          UserDataResponse(
            data = MediaItemFactory.moviesPagination().list,
            totalResults = MediaItemFactory.moviesPagination().totalResults,
            type = MediaType.MOVIE,
            canFetchMore = false,
            page = 1,
          ),
        ),
      )
      awaitComplete()
    }
  }
}
