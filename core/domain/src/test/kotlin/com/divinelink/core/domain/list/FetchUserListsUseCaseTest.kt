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
import com.divinelink.core.testing.repository.TestListRepository
import com.divinelink.core.testing.storage.FakeAccountStorage
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

  @Test
  fun `test fetch lists when account storage account id is null emits unauthenticated`() = runTest {
    val storage = createSessionStorage(
      accountDetailsId = null,
      v4AccountId = "1234",
      accessToken = AccessTokenFactory.valid(),
    )

    val useCase = FetchUserListsUseCase(
      storage = storage,
      repository = repository.mock,
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
    }
  }

  @Test
  fun `test fetch lists when observers to accountId changes`() = runTest {
    val storage = createSessionStorage(
      accountDetailsId = null,
      v4AccountId = "1234",
      accessToken = AccessTokenFactory.valid(),
    )

    repository.mockFetchUserLists(
      flowOf(Resource.Success(ListItemFactory.page1())),
    )

    val useCase = FetchUserListsUseCase(
      storage = storage,
      repository = repository.mock,
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

      storage.accountStorage.setAccountDetails(AccountDetailsFactory.Pinkman())

      assertThat(awaitItem()).isEqualTo(Result.success(ListItemFactory.page1()))
    }
  }

  @Test
  fun `test fetch lists with loading resource`() = runTest {
    val storage = createSessionStorage(
      accountDetailsId = AccountDetailsFactory.Pinkman().id.toString(),
      v4AccountId = "1234",
      accessToken = AccessTokenFactory.valid(),
    )

    repository.mockFetchUserLists(
      flowOf(Resource.Loading(ListItemFactory.page1())),
    )

    val useCase = FetchUserListsUseCase(
      storage = storage,
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    useCase.invoke(
      UserListsParameters(
        page = 1,
      ),
    ).test {
      assertThat(awaitItem()).isEqualTo(Result.success(ListItemFactory.page1()))
    }
  }

  @Test
  fun `test fetch lists with loading resource with null content`() = runTest {
    val storage = createSessionStorage(
      accountDetailsId = AccountDetailsFactory.Pinkman().id.toString(),
      v4AccountId = "1234",
      accessToken = AccessTokenFactory.valid(),
    )

    repository.mockFetchUserLists(
      flowOf(Resource.Loading(null)),
    )

    val useCase = FetchUserListsUseCase(
      storage = storage,
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    useCase.invoke(
      UserListsParameters(
        page = 1,
      ),
    ).test {
      expectNoEvents()
    }
  }

  @Test
  fun `test fetch lists with success resource with null content`() = runTest {
    val storage = createSessionStorage(
      accountDetailsId = AccountDetailsFactory.Pinkman().id.toString(),
      v4AccountId = "1234",
      accessToken = AccessTokenFactory.valid(),
    )

    repository.mockFetchUserLists(
      flowOf(Resource.Success(null)),
    )

    val useCase = FetchUserListsUseCase(
      storage = storage,
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    useCase.invoke(
      UserListsParameters(
        page = 1,
      ),
    ).test {
      expectNoEvents()
    }
  }

  @Test
  fun `test fetch lists when v4 account id is null`() = runTest {
    val storage = createSessionStorage(
      accountDetailsId = "12345",
      v4AccountId = null,
      accessToken = AccessTokenFactory.valid(),
    )

    repository.mockFetchUserLists(
      flowOf(Resource.Success(ListItemFactory.page1())),
    )

    val useCase = FetchUserListsUseCase(
      storage = storage,
      repository = repository.mock,
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

      expectNoEvents()
    }
  }

  @Test
  fun `test fetch lists when access token is null`() = runTest {
    val storage = createSessionStorage(
      accountDetailsId = "12345",
      v4AccountId = "1234",
      accessToken = null,
    )

    repository.mockFetchUserLists(
      flowOf(Resource.Success(ListItemFactory.page1())),
    )

    val useCase = FetchUserListsUseCase(
      storage = storage,
      repository = repository.mock,
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

      expectNoEvents()
    }
  }

  @Test
  fun `test fetch lists with failure`() = runTest {
    val storage = createSessionStorage(
      accountDetailsId = "12345",
      v4AccountId = "1234",
      accessToken = AccessTokenFactory.valid(),
    )

    repository.mockFetchUserLists(
      flowOf(Resource.Error(Exception("Failed to fetch lists"))),
    )

    val useCase = FetchUserListsUseCase(
      storage = storage,
      repository = repository.mock,
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

      expectNoEvents()
    }
  }

  private fun createSessionStorage(
    v4AccountId: String?,
    accountDetailsId: String?,
    accessToken: AccessToken? = null,
  ) = SessionStorage(
    encryptedStorage = FakeEncryptedPreferenceStorage(
      sessionId = "123",
      accessToken = accessToken?.accessToken,
      tmdbAccountId = v4AccountId,
    ),
    accountStorage = FakeAccountStorage(
      accountDetails = accountDetailsId?.let {
        AccountDetailsFactory.Pinkman().copy(id = it.toInt())
      },
    ),
  )
}
