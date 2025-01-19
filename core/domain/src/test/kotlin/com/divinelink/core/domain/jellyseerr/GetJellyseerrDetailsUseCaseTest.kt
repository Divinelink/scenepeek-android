package com.divinelink.core.domain.jellyseerr

import app.cash.turbine.test
import com.divinelink.core.commons.domain.data
import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.fixtures.model.jellyseerr.JellyseerrAccountDetailsFactory
import com.divinelink.core.model.jellyseerr.JellyseerrAuthMethod
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.repository.TestJellyseerrRepository
import com.divinelink.core.testing.storage.FakePreferenceStorage
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test

class GetJellyseerrDetailsUseCaseTest {

  private lateinit var preferenceStorage: PreferenceStorage

  private val repository = TestJellyseerrRepository()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  @Test
  fun `test get jellyseerr account details with null storage data`() = runTest {
    preferenceStorage = FakePreferenceStorage()

    repository.mockGetLocalJellyseerrAccountDetails(JellyseerrAccountDetailsFactory.jellyseerr())

    val useCase = GetJellyseerrAccountDetailsUseCase(
      storage = preferenceStorage,
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    useCase.invoke(false).test {
      assertThat(awaitItem()).isEqualTo(Result.success(null))
      awaitComplete()
    }
  }

  @Test
  fun `test get jellyseerr account details with storage data`() = runTest {
    preferenceStorage = FakePreferenceStorage(
      jellyseerrAddress = "http://localhost:8096",
      jellyseerrAccount = "jellyseerrAccount",
      jellyseerrSignInMethod = JellyseerrAuthMethod.JELLYSEERR.name,
    )

    repository.mockGetLocalJellyseerrAccountDetails(JellyseerrAccountDetailsFactory.jellyseerr())

    val useCase = GetJellyseerrAccountDetailsUseCase(
      storage = preferenceStorage,
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    val result = useCase.invoke(false).first()

    assertThat(result.isSuccess).isTrue()
    assertThat(result.data).isEqualTo(JellyseerrAccountDetailsFactory.jellyseerr())
  }

  @Test
  fun `test get remote jellyseerr account details with null address`() = runTest {
    preferenceStorage = FakePreferenceStorage(
      jellyseerrAddress = null,
      jellyseerrAccount = "jellyseerrAccount",
      jellyseerrSignInMethod = JellyseerrAuthMethod.JELLYSEERR.name,
    )

    repository.mockGetLocalJellyseerrAccountDetails(JellyseerrAccountDetailsFactory.jellyseerr())
    repository.mockGetRemoteAccountDetails(
      Result.success(JellyseerrAccountDetailsFactory.jellyseerr()),
    )

    val useCase = GetJellyseerrAccountDetailsUseCase(
      storage = preferenceStorage,
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    useCase.invoke(true).test {
      assertThat(awaitItem()).isEqualTo(Result.success(null))
      awaitComplete()
    }
  }

  @Test
  fun `test get remote jellyseerr account details without local data and address`() = runTest {
    preferenceStorage = FakePreferenceStorage(
      jellyseerrAddress = "http://localhost:8096",
      jellyseerrAccount = "jellyseerrAccount",
      jellyseerrSignInMethod = JellyseerrAuthMethod.JELLYSEERR.name,
    )

    repository.mockGetLocalJellyseerrAccountDetails(null)
    repository.mockGetRemoteAccountDetails(
      Result.success(JellyseerrAccountDetailsFactory.jellyseerr()),
    )

    val useCase = GetJellyseerrAccountDetailsUseCase(
      storage = preferenceStorage,
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    useCase.invoke(true).test {
      assertThat(awaitItem().getOrNull()).isNull()
      assertThat(awaitItem().getOrNull()).isEqualTo(JellyseerrAccountDetailsFactory.jellyseerr())
      awaitComplete()
    }
  }

  @Test
  fun `test get remote jellyseerr account details with failure and no local data`() = runTest {
    preferenceStorage = FakePreferenceStorage(
      jellyseerrAddress = "http://localhost:8096",
      jellyseerrAccount = "jellyseerrAccount",
      jellyseerrSignInMethod = JellyseerrAuthMethod.JELLYSEERR.name,
    )

    repository.mockGetLocalJellyseerrAccountDetails(null)
    repository.mockGetRemoteAccountDetails(
      Result.failure(Exception("Error")),
    )

    val useCase = GetJellyseerrAccountDetailsUseCase(
      storage = preferenceStorage,
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    useCase.invoke(true).test {
      assertThat(awaitItem().getOrNull()).isNull()
      assertThat(awaitItem().getOrNull()).isNull()
      awaitComplete()
    }
  }
}
