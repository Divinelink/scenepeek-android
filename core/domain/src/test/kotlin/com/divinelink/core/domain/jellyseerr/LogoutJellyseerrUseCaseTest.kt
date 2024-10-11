package com.divinelink.core.domain.jellyseerr

import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.model.jellyseerr.JellyseerrAuthMethod
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.factories.storage.SessionStorageFactory
import com.divinelink.core.testing.factories.storage.SessionStorageFactory.toWzd
import com.divinelink.core.testing.repository.TestJellyseerrRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test

class LogoutJellyseerrUseCaseTest {

  private lateinit var sessionStorage: SessionStorage

  private val repository = TestJellyseerrRepository()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  @Test
  fun `test logout jellyseerr with null address`() = runTest {
    sessionStorage = SessionStorageFactory.empty()

    val useCase = LogoutJellyseerrUseCase(
      repository = repository.mock,
      sessionStorage = sessionStorage,
      dispatcher = testDispatcher,
    )

    val result = useCase.invoke(Unit).first()

    assertThat(result.isFailure).isTrue()
    assertThat(result.exceptionOrNull()).isInstanceOf(Exception::class.java)
    assertThat(result.exceptionOrNull()?.message).isEqualTo("No address found.")
  }

  @Test
  fun `test logout jellyseerr with success`() = runTest {
    sessionStorage = SessionStorageFactory.empty().toWzd {
      withJellyseerrAddress("http://localhost:8096")
    }

    repository.mockLogout(Result.success(Unit))

    val useCase = LogoutJellyseerrUseCase(
      repository = repository.mock,
      sessionStorage = sessionStorage,
      dispatcher = testDispatcher,
    )

    val result = useCase.invoke(Unit).first()

    assertThat(result.isSuccess).isTrue()
    assertThat(result.getOrNull()).isEqualTo("http://localhost:8096")
  }

  @Test
  fun `test logout jellyseerr with success clear jellyseerr session`() = runTest {
    sessionStorage = SessionStorageFactory.empty().toWzd {
      withJellyseerrAddress("http://localhost:8096")
      withJellyseerrAccount("jellyseerrAccount")
      withJellyseerrSignInMethod(JellyseerrAuthMethod.JELLYSEERR.name)
      withJellyseerrAuthCookie("jellyseerrAuthCookie")
    }

    assertThat(sessionStorage.storage.jellyseerrAddress.first()).isEqualTo("http://localhost:8096")
    assertThat(sessionStorage.storage.jellyseerrAccount.first()).isEqualTo("jellyseerrAccount")
    assertThat(sessionStorage.storage.jellyseerrAuthMethod.first()).isEqualTo(
      JellyseerrAuthMethod.JELLYSEERR.name,
    )
    assertThat(sessionStorage.encryptedStorage.jellyseerrAuthCookie).isEqualTo(
      "jellyseerrAuthCookie",
    )

    repository.mockLogout(Result.success(Unit))

    val useCase = LogoutJellyseerrUseCase(
      repository = repository.mock,
      sessionStorage = sessionStorage,
      dispatcher = testDispatcher,
    )

    val result = useCase.invoke(Unit).first()

    assertThat(result.isSuccess).isTrue()

    repository.verifyClearJellyseerrAccountDetails()

    assertThat(sessionStorage.storage.jellyseerrAddress.first()).isNull()
    assertThat(sessionStorage.storage.jellyseerrAccount.first()).isNull()
    assertThat(sessionStorage.storage.jellyseerrAuthMethod.first()).isNull()
    assertThat(sessionStorage.encryptedStorage.jellyseerrAuthCookie).isNull()
  }

  @Test
  fun `test logout jellyseerr with failure`() = runTest {
    sessionStorage = SessionStorageFactory.empty().toWzd {
      withJellyseerrAddress("http://localhost:8096")
    }

    repository.mockLogout(Result.failure(Exception("Logout failed.")))

    val useCase = LogoutJellyseerrUseCase(
      repository = repository.mock,
      sessionStorage = sessionStorage,
      dispatcher = testDispatcher,
    )

    val result = useCase.invoke(Unit).first()

    assertThat(result.isFailure).isTrue()
    assertThat(result.exceptionOrNull()).isInstanceOf(Exception::class.java)
    assertThat(result.exceptionOrNull()?.message).isEqualTo("Logout failed.")
  }
}
