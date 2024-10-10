package com.divinelink.core.domain.jellyseerr

import com.divinelink.core.datastore.EncryptedStorage
import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.model.Password
import com.divinelink.core.model.Username
import com.divinelink.core.model.jellyseerr.JellyseerrLoginMethod
import com.divinelink.core.model.jellyseerr.JellyseerrLoginParams
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.factories.model.jellyseerr.JellyseerrAccountDetailsFactory
import com.divinelink.core.testing.repository.TestJellyseerrRepository
import com.divinelink.core.testing.storage.FakeEncryptedPreferenceStorage
import com.divinelink.core.testing.storage.FakePreferenceStorage
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test

class LoginJellyseerrUseCaseTest {

  private lateinit var preferenceStorage: PreferenceStorage
  private lateinit var encryptedStorage: EncryptedStorage

  private val repository = TestJellyseerrRepository()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  @Test
  fun `test loginJellyseerr with null parameters throws exception`() = runTest {
    preferenceStorage = FakePreferenceStorage()
    encryptedStorage = FakeEncryptedPreferenceStorage()

    val useCase = LoginJellyseerrUseCase(
      repository = repository.mock,
      storage = preferenceStorage,
      dispatcher = testDispatcher,
      encryptedStorage = encryptedStorage,
    )

    useCase.invoke(null).collect {
      assertThat(it.isFailure).isTrue()
      assertThat(it.exceptionOrNull()).isInstanceOf(IllegalArgumentException::class.java)
    }
  }

  @Test
  fun `test loginJellyseerr with Jellyfin login method`() = runTest {
    preferenceStorage = FakePreferenceStorage()
    encryptedStorage = FakeEncryptedPreferenceStorage()

    repository.mockSignInWithJellyfin(Result.success(JellyseerrAccountDetailsFactory.jellyfin()))

    val useCase = LoginJellyseerrUseCase(
      repository = repository.mock,
      storage = preferenceStorage,
      dispatcher = testDispatcher,
      encryptedStorage = encryptedStorage,
    )

    assertThat(encryptedStorage.jellyseerrPassword).isNull()

    useCase.invoke(
      JellyseerrLoginParams(
        username = Username("jellyfinUsername"),
        password = Password("password"),
        address = "http://localhost:8096",
        signInMethod = JellyseerrLoginMethod.JELLYFIN,
      ),
    ).collect {
      assertThat(it.isSuccess).isTrue()
      assertThat(it.getOrNull()).isEqualTo(JellyseerrAccountDetailsFactory.jellyfin())
      assertThat(preferenceStorage.jellyseerrAccount.first()).isEqualTo("jellyfinUsername")
      assertThat(preferenceStorage.jellyseerrAddress.first()).isEqualTo("http://localhost:8096")
      assertThat(
        preferenceStorage.jellyseerrSignInMethod.first(),
      ).isEqualTo(JellyseerrLoginMethod.JELLYFIN.name)
      assertThat(encryptedStorage.jellyseerrPassword).isEqualTo("password")
    }
  }

  @Test
  fun `test loginJellyseerr with Jellyseerr login method`() = runTest {
    preferenceStorage = FakePreferenceStorage()
    encryptedStorage = FakeEncryptedPreferenceStorage()

    repository.mockSignInWithJellyseerr(
      Result.success(JellyseerrAccountDetailsFactory.jellyseerr()),
    )

    val useCase = LoginJellyseerrUseCase(
      repository = repository.mock,
      storage = preferenceStorage,
      dispatcher = testDispatcher,
      encryptedStorage = encryptedStorage,
    )

    assertThat(encryptedStorage.jellyseerrPassword).isNull()

    useCase.invoke(
      JellyseerrLoginParams(
        username = Username("jellyseerrUsername"),
        password = Password("password"),
        address = "http://localhost:8096",
        signInMethod = JellyseerrLoginMethod.JELLYSEERR,
      ),
    ).collect {
      assertThat(it.isSuccess).isTrue()
      assertThat(it.getOrNull()).isEqualTo(JellyseerrAccountDetailsFactory.jellyseerr())
      assertThat(preferenceStorage.jellyseerrAccount.first()).isEqualTo("jellyseerrUsername")
      assertThat(preferenceStorage.jellyseerrAddress.first()).isEqualTo("http://localhost:8096")
      assertThat(
        preferenceStorage.jellyseerrSignInMethod.first(),
      ).isEqualTo(JellyseerrLoginMethod.JELLYSEERR.name)
      assertThat(encryptedStorage.jellyseerrPassword).isEqualTo("password")
    }
  }

  @Test
  fun `test loginJellyseerr with Jellyseerr login method and error`() = runTest {
    preferenceStorage = FakePreferenceStorage()
    encryptedStorage = FakeEncryptedPreferenceStorage()

    repository.mockSignInWithJellyseerr(Result.failure(Exception("error")))

    val useCase = LoginJellyseerrUseCase(
      repository = repository.mock,
      storage = preferenceStorage,
      dispatcher = testDispatcher,
      encryptedStorage = encryptedStorage,
    )

    useCase.invoke(
      JellyseerrLoginParams(
        username = Username("jellyseerrUsername"),
        password = Password("password"),
        address = "http://localhost:8096",
        signInMethod = JellyseerrLoginMethod.JELLYSEERR,
      ),
    ).collect {
      assertThat(it.isFailure).isTrue()
    }
  }

  @Test
  fun `test loginJellyseerr with Jellyfin login method and error`() = runTest {
    preferenceStorage = FakePreferenceStorage()
    encryptedStorage = FakeEncryptedPreferenceStorage()

    repository.mockSignInWithJellyfin(Result.failure(Exception("error")))

    val useCase = LoginJellyseerrUseCase(
      repository = repository.mock,
      storage = preferenceStorage,
      dispatcher = testDispatcher,
      encryptedStorage = encryptedStorage,
    )

    useCase.invoke(
      JellyseerrLoginParams(
        username = Username("jellyfinUsername"),
        password = Password("password"),
        address = "http://localhost:8096",
        signInMethod = JellyseerrLoginMethod.JELLYFIN,
      ),
    ).collect {
      assertThat(it.isFailure).isTrue()
    }
  }
}
