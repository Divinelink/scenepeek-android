package com.divinelink.core.domain.jellyseerr

import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.factories.datastore.auth.JellyseerrAccountFactory
import com.divinelink.core.testing.repository.TestAuthRepository
import com.divinelink.core.testing.repository.TestJellyseerrRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test

class LogoutJellyseerrUseCaseTest {

  private val repository = TestJellyseerrRepository()
  private val authRepository = TestAuthRepository()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  @Test
  fun `test logout jellyseerr with null account`() = runTest {
    authRepository.mockSelectedJellyseerrAccount(null)

    val useCase = LogoutJellyseerrUseCase(
      repository = repository.mock,
      authRepository = authRepository.mock,
      dispatcher = testDispatcher,
    )

    val result = useCase.invoke(Unit)

    assertThat(result).isEqualTo(Result.success(Unit))
    authRepository.verifyClearSelectedJellyseerrAccount()
    repository.verifyClearJellyseerrAccountDetails()
  }

  @Test
  fun `test logout jellyseerr with success`() = runTest {
    authRepository.mockSelectedJellyseerrAccount(JellyseerrAccountFactory.zabaob())

    repository.mockLogout(Result.success(Unit))

    val useCase = LogoutJellyseerrUseCase(
      repository = repository.mock,
      authRepository = authRepository.mock,
      dispatcher = testDispatcher,
    )

    val result = useCase.invoke(Unit)

    assertThat(result.isSuccess).isTrue()
    assertThat(result).isEqualTo(Result.success(Unit))
    authRepository.verifyClearSelectedJellyseerrAccount()
    repository.verifyClearJellyseerrAccountDetails()
  }

  @Test
  fun `test logout jellyseerr with failure`() = runTest {
    authRepository.mockSelectedJellyseerrAccount(JellyseerrAccountFactory.zabaob())

    repository.mockLogout(Result.failure(Exception("Logout failed.")))

    val useCase = LogoutJellyseerrUseCase(
      repository = repository.mock,
      authRepository = authRepository.mock,
      dispatcher = testDispatcher,
    )

    val result = useCase.invoke(Unit)

    assertThat(result.isFailure).isTrue()
    assertThat(result.exceptionOrNull()).isInstanceOf(Exception::class.java)
    assertThat(result.exceptionOrNull()?.message).isEqualTo("Logout failed.")

    authRepository.verifyClearSelectedJellyseerrAccount()
    repository.verifyClearJellyseerrAccountDetails()
  }

  @Test
  fun `test logout jellyseerr with failure clears session data`() = runTest {
    authRepository.mockSelectedJellyseerrAccount(JellyseerrAccountFactory.zabaob())

    repository.mockLogout(Result.failure(Exception("Logout failed.")))

    val useCase = LogoutJellyseerrUseCase(
      repository = repository.mock,
      authRepository = authRepository.mock,
      dispatcher = testDispatcher,
    )

    val response = useCase.invoke(Unit)

    repository.verifyClearJellyseerrAccountDetails()
    authRepository.verifyClearSelectedJellyseerrAccount()

    assertThat(response.toString()).isEqualTo(
      Result.failure<Exception>(Exception("Logout failed.")).toString(),
    )
  }
}
