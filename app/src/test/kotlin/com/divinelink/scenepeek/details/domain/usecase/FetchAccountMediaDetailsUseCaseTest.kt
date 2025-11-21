package com.divinelink.scenepeek.details.domain.usecase

import app.cash.turbine.test
import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.details.model.MediaDetailsParams
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.fixtures.model.account.AccountDetailsFactory
import com.divinelink.core.model.exception.SessionException
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.factories.storage.SessionStorageFactory
import com.divinelink.core.testing.repository.TestAuthRepository
import com.divinelink.core.testing.repository.TestDetailsRepository
import com.divinelink.factories.details.domain.model.account.AccountMediaDetailsFactory
import com.divinelink.feature.details.media.usecase.FetchAccountMediaDetailsUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test

class FetchAccountMediaDetailsUseCaseTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private lateinit var repository: TestDetailsRepository
  private lateinit var authRepository: TestAuthRepository

  private lateinit var sessionStorage: SessionStorage

  @Before
  fun setUp() {
    repository = TestDetailsRepository()
    authRepository = TestAuthRepository()
  }

  @Test
  fun `test user with no session id cannot fetch account media details`() = runTest {
    sessionStorage = SessionStorageFactory.empty()

    authRepository.mockTMDBAccount(AccountDetailsFactory.Pinkman())
    val useCase = FetchAccountMediaDetailsUseCase(
      sessionStorage = sessionStorage,
      repository = repository.mock,
      authRepository = authRepository.mock,
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
    sessionStorage = SessionStorageFactory.full()
    sessionStorage = SessionStorageFactory.full()

    authRepository.mockTMDBAccount(AccountDetailsFactory.Pinkman())
    repository.mockFetchAccountMediaDetails(
      response = Result.success(AccountMediaDetailsFactory.Rated()),
    )

    val useCase = FetchAccountMediaDetailsUseCase(
      sessionStorage = sessionStorage,
      repository = repository.mock,
      authRepository = authRepository.mock,
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
    sessionStorage = SessionStorageFactory.full()

    authRepository.mockTMDBAccount(AccountDetailsFactory.Pinkman())
    repository.mockFetchAccountMediaDetails(
      response = Result.success(AccountMediaDetailsFactory.Rated()),
    )

    val useCase = FetchAccountMediaDetailsUseCase(
      sessionStorage = sessionStorage,
      repository = repository.mock,
      authRepository = authRepository.mock,
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
    sessionStorage = SessionStorageFactory.full()

    val useCase = FetchAccountMediaDetailsUseCase(
      sessionStorage = sessionStorage,
      repository = repository.mock,
      authRepository = authRepository.mock,
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

  @Test
  fun `test fetch account media details obverses tmdb account`() = runTest {
    sessionStorage = SessionStorageFactory.full()

    authRepository.mockTMDBAccount(
      flowOf(
        null,
        AccountDetailsFactory.Pinkman(),
      ),
    )
    repository.mockFetchAccountMediaDetails(
      flowOf(
        Result.success(AccountMediaDetailsFactory.initial()),
        Result.success(AccountMediaDetailsFactory.Rated()),
      ),
    )

    val useCase = FetchAccountMediaDetailsUseCase(
      sessionStorage = sessionStorage,
      repository = repository.mock,
      authRepository = authRepository.mock,
      dispatcher = testDispatcher,
    )

    useCase.invoke(
      MediaDetailsParams(
        id = 0,
        mediaType = MediaType.MOVIE,
      ),
    ).test {
      assertThat(awaitItem()).isEqualTo(Result.success(AccountMediaDetailsFactory.initial()))
      assertThat(awaitItem()).isEqualTo(Result.success(AccountMediaDetailsFactory.Rated()))

      assertThat(awaitItem()).isEqualTo(Result.success(AccountMediaDetailsFactory.initial()))
      assertThat(awaitItem()).isEqualTo(Result.success(AccountMediaDetailsFactory.Rated()))

      awaitComplete()
    }
  }
}
