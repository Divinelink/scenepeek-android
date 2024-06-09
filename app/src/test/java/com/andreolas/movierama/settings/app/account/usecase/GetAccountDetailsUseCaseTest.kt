package com.andreolas.movierama.settings.app.account.usecase

import com.andreolas.movierama.MainDispatcherRule
import com.andreolas.movierama.fakes.repository.FakeSessionRepository
import com.andreolas.movierama.session.SessionStorage
import com.andreolas.movierama.test.util.fakes.FakeEncryptedPreferenceStorage
import com.andreolas.movierama.test.util.fakes.FakePreferenceStorage
import com.divinelink.core.model.account.AccountDetails
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetAccountDetailsUseCaseTest {

  private lateinit var repository: FakeSessionRepository

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  @Before
  fun setUp() {
    repository = FakeSessionRepository()
  }

  @Test
  fun `given no sessionId, when getAccountDetails is called, then return failure`() = runTest {
    val sessionStorage = createSessionStorage()

    val useCase = GetAccountDetailsUseCase(
      sessionStorage = sessionStorage,
      repository = repository.mock,
      dispatcher = testDispatcher
    )

    val result = useCase.invoke(Unit)

    assertThat(result.first().isFailure).isTrue()
  }

  @Test
  fun `given sessionId, when getAccountDetails is called, then return success`() = runTest {
    val sessionStorage = createSessionStorage(sessionId = "session_id")

    repository.mockGetAccountDetails(
      Result.success(
        AccountDetails(
          id = 1234,
          username = "username",
          name = "name",
        )
      )
    )

    val useCase = GetAccountDetailsUseCase(
      sessionStorage = sessionStorage,
      repository = repository.mock,
      dispatcher = testDispatcher
    )

    val result = useCase.invoke(Unit)

    assertThat(result.first().isSuccess).isTrue()
    assertThat(sessionStorage.accountId.first()).isEqualTo("1234")
  }

  private fun createSessionStorage(sessionId: String? = null) = SessionStorage(
    storage = FakePreferenceStorage(),
    encryptedStorage = FakeEncryptedPreferenceStorage(sessionId = sessionId)
  )
}