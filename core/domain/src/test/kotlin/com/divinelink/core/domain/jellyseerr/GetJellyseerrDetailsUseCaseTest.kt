package com.divinelink.core.domain.jellyseerr

import com.divinelink.core.commons.domain.data
import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.model.jellyseerr.JellyseerrLoginMethod
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.factories.model.jellyseerr.JellyseerrAccountDetailsFactory
import com.divinelink.core.testing.repository.TestJellyseerrRepository
import com.divinelink.core.testing.storage.FakePreferenceStorage
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class GetJellyseerrDetailsUseCaseTest {

  private lateinit var preferenceStorage: PreferenceStorage

  private val repository = TestJellyseerrRepository()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  @Test
  fun `test get jellyseerr account details with null storage data`() = runTest {
    preferenceStorage = FakePreferenceStorage()

    repository.mockGetJellyseerrAccountDetails(JellyseerrAccountDetailsFactory.jellyseerr())

    val useCase = GetJellyseerrDetailsUseCase(
      storage = preferenceStorage,
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    val result = useCase.invoke(Unit).first()

    assertThat(result.isFailure).isTrue()
    assertThat(result.exceptionOrNull()).isInstanceOf(Exception::class.java)
  }

  @Test
  fun `test get jellyseerr account details with storage data`() = runTest {
    preferenceStorage = FakePreferenceStorage(
      jellyseerrAddress = "http://localhost:8096",
      jellyseerrAccount = "jellyseerrAccount",
      jellyseerrSignInMethod = JellyseerrLoginMethod.JELLYSEERR.name,
    )

    repository.mockGetJellyseerrAccountDetails(JellyseerrAccountDetailsFactory.jellyseerr())

    val useCase = GetJellyseerrDetailsUseCase(
      storage = preferenceStorage,
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    val result = useCase.invoke(Unit).first()

    assertThat(result.isSuccess).isTrue()
    assertThat(result.data).isEqualTo(JellyseerrAccountDetailsFactory.jellyseerr())
  }
}
