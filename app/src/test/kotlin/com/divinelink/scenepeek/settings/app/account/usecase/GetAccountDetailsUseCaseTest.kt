package com.divinelink.scenepeek.settings.app.account.usecase

import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.domain.GetAccountDetailsUseCase
import com.divinelink.core.fixtures.model.account.AccountDetailsFactory
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.repository.TestSessionRepository
import com.divinelink.core.testing.storage.FakeEncryptedPreferenceStorage
import com.divinelink.core.testing.storage.FakePreferenceStorage
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test

class GetAccountDetailsUseCaseTest {

  private lateinit var repository: TestSessionRepository

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  @Before
  fun setUp() {
    repository = TestSessionRepository()
  }

  @Test
  fun `given no sessionId, when getAccountDetails is called, then return failure`() = runTest {
    val sessionStorage = createSessionStorage()

    val useCase = GetAccountDetailsUseCase(
      sessionStorage = sessionStorage,
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    val result = useCase.invoke(Unit)

    assertThat(result.first().isFailure).isTrue()
  }

  @Test
  fun `given sessionId, when getAccountDetails is called, then return success`() = runTest {
    val sessionStorage = createSessionStorage(sessionId = "session_id")

    repository.mockGetAccountDetails(
      Result.success(
        AccountDetailsFactory.Pinkman(),
      ),
    )

    val useCase = GetAccountDetailsUseCase(
      sessionStorage = sessionStorage,
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    val result = useCase.invoke(Unit)

    assertThat(result.first().isSuccess).isTrue()
    assertThat(sessionStorage.accountId.first()).isEqualTo("1")
  }

  private fun createSessionStorage(sessionId: String? = null) = SessionStorage(
    storage = FakePreferenceStorage(),
    encryptedStorage = FakeEncryptedPreferenceStorage(sessionId = sessionId),
  )
}
