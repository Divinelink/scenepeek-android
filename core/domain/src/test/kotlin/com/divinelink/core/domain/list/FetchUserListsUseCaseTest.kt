package com.divinelink.core.domain.list

import app.cash.turbine.test
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.fixtures.model.account.AccountDetailsFactory
import com.divinelink.core.fixtures.model.list.ListItemFactory
import com.divinelink.core.fixtures.model.session.AccessTokenFactory
import com.divinelink.core.model.exception.SessionException
import com.divinelink.core.model.session.AccessToken
import com.divinelink.core.network.Resource
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.repository.TestAuthRepository
import com.divinelink.core.testing.repository.TestListRepository
import com.divinelink.core.testing.storage.FakeEncryptedPreferenceStorage
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test

class FetchUserListsUseCaseTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private val repository = TestListRepository()
  private val authRepository = TestAuthRepository()

  @Test
  fun `test fetch lists when account storage account id is null emits unauthenticated`() = runTest {
    val storage = createSessionStorage(
      v4AccountId = "1234",
      accessToken = AccessTokenFactory.valid(),
    )
    authRepository.mockTMDBAccount(null)

    val useCase = FetchUserListsUseCase(
      storage = storage,
      repository = repository.mock,
      authRepository = authRepository.mock,
      dispatcher = testDispatcher,
    )

    useCase.invoke(
      UserListsParameters(
        page = 1,
      ),
    ).test {
      val awaitItem = awaitItem()

      assertThat(awaitItem.toString()).isEqualTo(
        Result.failure<Exception>(SessionException.Unauthenticated()).toString(),
      )
      awaitComplete()
    }
  }

  @Test
  fun `test fetch lists when observers to accountId changes`() = runTest {
    val storage = createSessionStorage(
      v4AccountId = "1234",
      accessToken = AccessTokenFactory.valid(),
    )

    authRepository.mockTMDBAccount(flowOf(null, AccountDetailsFactory.Pinkman()))
    repository.mockFetchUserLists(
      flowOf(Resource.Success(ListItemFactory.page1())),
    )

    val useCase = FetchUserListsUseCase(
      storage = storage,
      repository = repository.mock,
      authRepository = authRepository.mock,
      dispatcher = testDispatcher,
    )

    useCase.invoke(
      UserListsParameters(
        page = 1,
      ),
    ).test {
      val firstEmission = awaitItem()

      assertThat(firstEmission.toString()).isEqualTo(
        Result.failure<Exception>(SessionException.Unauthenticated()).toString(),
      )

      assertThat(awaitItem()).isEqualTo(Result.success(ListItemFactory.page1()))

      awaitComplete()
    }
  }

  @Test
  fun `test fetch lists with loading resource`() = runTest {
    val storage = createSessionStorage(
      v4AccountId = "1234",
      accessToken = AccessTokenFactory.valid(),
    )

    authRepository.mockTMDBAccount(AccountDetailsFactory.Pinkman())
    repository.mockFetchUserLists(
      flowOf(Resource.Loading(ListItemFactory.page1())),
    )

    val useCase = FetchUserListsUseCase(
      storage = storage,
      repository = repository.mock,
      authRepository = authRepository.mock,
      dispatcher = testDispatcher,
    )

    useCase.invoke(
      UserListsParameters(
        page = 1,
      ),
    ).test {
      assertThat(awaitItem()).isEqualTo(Result.success(ListItemFactory.page1()))
      awaitComplete()
    }
  }

  @Test
  fun `test fetch lists with loading resource with null content`() = runTest {
    val storage = createSessionStorage(
      v4AccountId = "1234",
      accessToken = AccessTokenFactory.valid(),
    )

    authRepository.mockTMDBAccount(AccountDetailsFactory.Pinkman())
    repository.mockFetchUserLists(
      flowOf(Resource.Loading(null)),
    )

    val useCase = FetchUserListsUseCase(
      storage = storage,
      repository = repository.mock,
      authRepository = authRepository.mock,
      dispatcher = testDispatcher,
    )

    useCase.invoke(
      UserListsParameters(
        page = 1,
      ),
    ).test {
      awaitComplete()
    }
  }

  @Test
  fun `test fetch lists with success resource with null content`() = runTest {
    val storage = createSessionStorage(
      v4AccountId = "1234",
      accessToken = AccessTokenFactory.valid(),
    )

    authRepository.mockTMDBAccount(AccountDetailsFactory.Pinkman())
    repository.mockFetchUserLists(
      flowOf(Resource.Success(null)),
    )

    val useCase = FetchUserListsUseCase(
      storage = storage,
      repository = repository.mock,
      authRepository = authRepository.mock,
      dispatcher = testDispatcher,
    )

    useCase.invoke(
      UserListsParameters(
        page = 1,
      ),
    ).test {
      awaitComplete()
    }
  }

  @Test
  fun `test fetch lists when v4 account id is null`() = runTest {
    val storage = createSessionStorage(
      v4AccountId = null,
      accessToken = AccessTokenFactory.valid(),
    )

    authRepository.mockTMDBAccount(AccountDetailsFactory.Pinkman())
    repository.mockFetchUserLists(
      flowOf(Resource.Success(ListItemFactory.page1())),
    )

    val useCase = FetchUserListsUseCase(
      storage = storage,
      repository = repository.mock,
      authRepository = authRepository.mock,
      dispatcher = testDispatcher,
    )

    useCase.invoke(
      UserListsParameters(
        page = 1,
      ),
    ).test {
      val firstEmission = awaitItem()

      assertThat(firstEmission.toString()).isEqualTo(
        Result.failure<Exception>(SessionException.Unauthenticated()).toString(),
      )

      awaitComplete()
    }
  }

  @Test
  fun `test fetch lists when access token is null`() = runTest {
    val storage = createSessionStorage(
      v4AccountId = "1234",
      accessToken = null,
    )

    authRepository.mockTMDBAccount(AccountDetailsFactory.Pinkman())
    repository.mockFetchUserLists(
      flowOf(Resource.Success(ListItemFactory.page1())),
    )

    val useCase = FetchUserListsUseCase(
      storage = storage,
      repository = repository.mock,
      authRepository = authRepository.mock,
      dispatcher = testDispatcher,
    )

    useCase.invoke(
      UserListsParameters(
        page = 1,
      ),
    ).test {
      val firstEmission = awaitItem()

      assertThat(firstEmission.toString()).isEqualTo(
        Result.failure<Exception>(SessionException.Unauthenticated()).toString(),
      )

      awaitComplete()
    }
  }

  @Test
  fun `test fetch lists with failure`() = runTest {
    val storage = createSessionStorage(
      v4AccountId = "1234",
      accessToken = AccessTokenFactory.valid(),
    )

    authRepository.mockTMDBAccount(AccountDetailsFactory.Pinkman())
    repository.mockFetchUserLists(
      flowOf(Resource.Error(Exception("Failed to fetch lists"))),
    )

    val useCase = FetchUserListsUseCase(
      storage = storage,
      repository = repository.mock,
      authRepository = authRepository.mock,
      dispatcher = testDispatcher,
    )

    useCase.invoke(
      UserListsParameters(
        page = 1,
      ),
    ).test {
      assertThat(awaitItem().toString()).isEqualTo(
        Result.failure<Exception>(Exception("Failed to fetch lists")).toString(),
      )

      awaitComplete()
    }
  }

  private fun createSessionStorage(
    v4AccountId: String?,
    accessToken: AccessToken? = null,
  ) = SessionStorage(
    encryptedStorage = FakeEncryptedPreferenceStorage(
      sessionId = "123",
      accessToken = accessToken?.accessToken,
      tmdbAccountId = v4AccountId,
    ),
  )
}
