package com.divinelink.core.domain.jellyseerr

import com.divinelink.core.datastore.auth.SavedState
import com.divinelink.core.fixtures.model.jellyseerr.JellyseerrAccountDetailsFactory
import com.divinelink.core.model.Password
import com.divinelink.core.model.Username
import com.divinelink.core.model.jellyseerr.JellyseerrAuthMethod
import com.divinelink.core.model.jellyseerr.JellyseerrLoginData
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.repository.TestAuthRepository
import com.divinelink.core.testing.repository.TestJellyseerrRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test

class LoginJellyseerrUseCaseTest {

  private val repository = TestJellyseerrRepository()
  private val authRepository = TestAuthRepository()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  @Test
  fun `test loginJellyseerr with Jellyfin login method`() = runTest {
    repository.mockSignInWithJellyfin(Result.success(Unit))
    repository.mockGetRemoteAccountDetails(
      Result.success(JellyseerrAccountDetailsFactory.jellyfin()),
    )

    val useCase = LoginJellyseerrUseCase(
      repository = repository.mock,
      authRepository = authRepository.mock,
      dispatcher = testDispatcher,
    )

    useCase.invoke(
      JellyseerrLoginData(
        username = Username("jellyfinUsername"),
        password = Password("password"),
        address = "http://localhost:8096",
        authMethod = JellyseerrAuthMethod.JELLYFIN,
      ),
    ).collect {
      assertThat(it.isSuccess).isTrue()
      assertThat(it.getOrNull()).isEqualTo(Unit)
      authRepository.verifyAccountUpdated(
        SavedState.JellyseerrAccount(
          account = "jellyfinUsername",
          password = "password",
          address = "http://localhost:8096",
          authMethod = JellyseerrAuthMethod.JELLYFIN,
        ),
      )
    }
  }

  @Test
  fun `test loginJellyseerr with Jellyseerr login method`() = runTest {
    repository.mockSignInWithJellyseerr(Result.success(Unit))
    repository.mockGetRemoteAccountDetails(
      Result.success(JellyseerrAccountDetailsFactory.jellyseerr()),
    )

    val useCase = LoginJellyseerrUseCase(
      repository = repository.mock,
      authRepository = authRepository.mock,
      dispatcher = testDispatcher,
    )

    useCase.invoke(
      JellyseerrLoginData(
        username = Username("jellyseerrUsername"),
        password = Password("password"),
        address = "http://localhost:8096",
        authMethod = JellyseerrAuthMethod.JELLYSEERR,
      ),
    ).collect {
      assertThat(it.isSuccess).isTrue()
      assertThat(it.getOrNull()).isEqualTo(Unit)
      authRepository.verifyAccountUpdated(
        SavedState.JellyseerrAccount(
          account = "jellyseerrUsername",
          password = "password",
          address = "http://localhost:8096",
          authMethod = JellyseerrAuthMethod.JELLYSEERR,
        ),
      )
    }
  }

  @Test
  fun `test loginJellyseerr with jellyseerr auth method but failed remote details`() = runTest {
    repository.mockSignInWithJellyseerr(Result.success(Unit))
    repository.mockGetRemoteAccountDetails(
      Result.failure(Exception()),
    )

    val useCase = LoginJellyseerrUseCase(
      repository = repository.mock,
      authRepository = authRepository.mock,
      dispatcher = testDispatcher,
    )

    useCase.invoke(
      JellyseerrLoginData(
        username = Username("jellyseerrUsername"),
        password = Password("password"),
        address = "http://localhost:8096",
        authMethod = JellyseerrAuthMethod.JELLYSEERR,
      ),
    ).collect {
      assertThat(it.isFailure).isTrue()
      assertThat(it.getOrNull()).isNull()
      authRepository.verifyNoInteractionsForUpdateJellyseerrAccount()
    }
  }

  @Test
  fun `test loginJellyseerr with jellyfin auth method but failed remote details`() = runTest {
    repository.mockSignInWithJellyfin(Result.success(Unit))
    repository.mockGetRemoteAccountDetails(
      Result.failure(Exception()),
    )

    val useCase = LoginJellyseerrUseCase(
      repository = repository.mock,
      authRepository = authRepository.mock,
      dispatcher = testDispatcher,
    )

    useCase.invoke(
      JellyseerrLoginData(
        username = Username("jellyseerrUsername"),
        password = Password("password"),
        address = "http://localhost:8096",
        authMethod = JellyseerrAuthMethod.JELLYFIN,
      ),
    ).collect {
      assertThat(it.isFailure).isTrue()
      assertThat(it.getOrNull()).isNull()
      authRepository.verifyNoInteractionsForUpdateJellyseerrAccount()
    }
  }

  @Test
  fun `test loginJellyseerr with Jellyseerr login method and error`() = runTest {
    repository.mockSignInWithJellyseerr(Result.failure(Exception("error")))

    val useCase = LoginJellyseerrUseCase(
      repository = repository.mock,
      authRepository = authRepository.mock,
      dispatcher = testDispatcher,
    )

    useCase.invoke(
      JellyseerrLoginData(
        username = Username("jellyseerrUsername"),
        password = Password("password"),
        address = "http://localhost:8096",
        authMethod = JellyseerrAuthMethod.JELLYSEERR,
      ),
    ).collect {
      assertThat(it.isFailure).isTrue()
    }
  }

  @Test
  fun `test loginJellyseerr with Jellyfin login method and error`() = runTest {
    repository.mockSignInWithJellyfin(Result.failure(Exception("error")))

    val useCase = LoginJellyseerrUseCase(
      repository = repository.mock,
      authRepository = authRepository.mock,
      dispatcher = testDispatcher,
    )

    useCase.invoke(
      JellyseerrLoginData(
        username = Username("jellyfinUsername"),
        password = Password("password"),
        address = "http://localhost:8096",
        authMethod = JellyseerrAuthMethod.JELLYFIN,
      ),
    ).collect {
      assertThat(it.isFailure).isTrue()
    }
  }
}
