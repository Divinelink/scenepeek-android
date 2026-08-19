package com.divinelink.core.domain.jellyseerr

import com.divinelink.core.datastore.auth.SavedState
import com.divinelink.core.fixtures.model.jellyseerr.JellyseerrProfileFactory
import com.divinelink.core.model.Address
import com.divinelink.core.model.Password
import com.divinelink.core.model.Username
import com.divinelink.core.model.jellyseerr.SeerrAuthMethod
import com.divinelink.core.model.jellyseerr.login.SeerrCredentials
import com.divinelink.core.model.jellyseerr.login.SeerrLoginData
import com.divinelink.core.network.Resource
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.repository.TestAuthRepository
import com.divinelink.core.testing.repository.TestJellyseerrRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
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
    repository.mockSignIn(Result.success(Unit))
    repository.mockGetJellyseerrProfile(
      flowOf(
        Resource.Loading(null),
        Resource.Success(JellyseerrProfileFactory.jellyfin()),
      ),
    )

    val useCase = LoginJellyseerrUseCase(
      repository = repository.mock,
      authRepository = authRepository.mock,
      dispatcher = testDispatcher,
    )

    useCase.invoke(
      SeerrLoginData(
        address = Address.from("http://localhost:8096"),
        credentials = SeerrCredentials.StandardAuth.Jellyfin(
          username = Username.from("jellyfinUsername"),
          password = Password("password"),
        ),
      ),
    ).collect {
      assertThat(it.isSuccess).isTrue()
      assertThat(it.getOrNull()).isEqualTo(Unit)
      authRepository.verifyAccountUpdated(
        SavedState.JellyseerrCredentials(
          account = "jellyfinUsername",
          password = "password",
          address = "http://localhost:8096",
          authMethod = SeerrAuthMethod.JELLYFIN,
        ),
      )
    }
  }

  @Test
  fun `test loginJellyseerr with Jellyseerr login method`() = runTest {
    repository.mockSignIn(Result.success(Unit))
    repository.mockGetJellyseerrProfile(
      flowOf(
        Resource.Loading(null),
        Resource.Success(JellyseerrProfileFactory.jellyseerr()),
      ),
    )

    val useCase = LoginJellyseerrUseCase(
      repository = repository.mock,
      authRepository = authRepository.mock,
      dispatcher = testDispatcher,
    )

    useCase.invoke(
      SeerrLoginData(
        address = Address.from("http://localhost:8096"),
        credentials = SeerrCredentials.Seerr(
          username = Username.from("jellyseerrUsername"),
          password = Password("password"),
        ),
      ),
    ).collect {
      assertThat(it.isSuccess).isTrue()
      assertThat(it.getOrNull()).isEqualTo(Unit)
      authRepository.verifyAccountUpdated(
        SavedState.JellyseerrCredentials(
          account = "jellyseerrUsername",
          password = "password",
          address = "http://localhost:8096",
          authMethod = SeerrAuthMethod.SEERR,
        ),
      )
    }
  }

  @Test
  fun `test loginJellyseerr with jellyseerr auth method but failed remote details`() = runTest {
    repository.mockSignIn(Result.success(Unit))
    repository.mockGetJellyseerrProfile(flowOf(Resource.Error(Exception())))

    val useCase = LoginJellyseerrUseCase(
      repository = repository.mock,
      authRepository = authRepository.mock,
      dispatcher = testDispatcher,
    )

    useCase.invoke(
      SeerrLoginData(
        address = Address.from("http://localhost:8096"),
        credentials = SeerrCredentials.Seerr(
          username = Username.from("jellyseerrUsername"),
          password = Password("password"),
        ),
      ),
    ).first()

    repository.verifyGetJellyseerrProfile()
  }

  @Test
  fun `test loginJellyseerr with jellyfin auth method but failed remote details`() = runTest {
    repository.mockSignIn(Result.success(Unit))
    repository.mockGetJellyseerrProfile(flowOf(Resource.Error(Exception())))

    val useCase = LoginJellyseerrUseCase(
      repository = repository.mock,
      authRepository = authRepository.mock,
      dispatcher = testDispatcher,
    )

    useCase.invoke(
      SeerrLoginData(
        address = Address.from("http://localhost:8096"),
        credentials = SeerrCredentials.StandardAuth.Jellyfin(
          username = Username.from("jellyseerrUsername"),
          password = Password("password"),
        ),
      ),
    ).first()

    repository.verifyGetJellyseerrProfile()
  }

  @Test
  fun `test loginJellyseerr with Jellyseerr login method and error`() = runTest {
    repository.mockSignIn(Result.failure(Exception("error")))

    val useCase = LoginJellyseerrUseCase(
      repository = repository.mock,
      authRepository = authRepository.mock,
      dispatcher = testDispatcher,
    )

    useCase.invoke(
      SeerrLoginData(
        address = Address.from("http://localhost:8096"),
        credentials = SeerrCredentials.Seerr(
          username = Username.from("jellyseerrUsername"),
          password = Password("password"),
        ),
      ),
    ).collect {
      assertThat(it.isFailure).isTrue()
    }
  }

  @Test
  fun `test loginJellyseerr with Jellyfin login method and error`() = runTest {
    repository.mockSignIn(Result.failure(Exception("error")))

    val useCase = LoginJellyseerrUseCase(
      repository = repository.mock,
      authRepository = authRepository.mock,
      dispatcher = testDispatcher,
    )

    useCase.invoke(
      SeerrLoginData(
        address = Address.from("http://localhost:8096"),
        credentials = SeerrCredentials.StandardAuth.Jellyfin(
          username = Username.from("jellyfinUsername"),
          password = Password("password"),
        ),
      ),
    ).collect {
      assertThat(it.isFailure).isTrue()
    }
  }
}
