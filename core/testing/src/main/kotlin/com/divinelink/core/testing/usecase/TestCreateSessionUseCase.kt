package com.divinelink.core.testing.usecase

import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.domain.session.CreateSessionUseCase
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.repository.TestSessionRepository
import com.divinelink.core.testing.storage.FakeEncryptedPreferenceStorage
import com.divinelink.core.testing.storage.FakePreferenceStorage
import org.junit.Rule
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions

class TestCreateSessionUseCase {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  val mock: CreateSessionUseCase = mock()

  fun useCase() = CreateSessionUseCase(
    repository = TestSessionRepository().mock,
    storage = SessionStorage(
      storage = FakePreferenceStorage(),
      encryptedStorage = FakeEncryptedPreferenceStorage(),
    ),
    dispatcher = mainDispatcherRule.testDispatcher,
  )

  suspend fun verifySessionInvoked(requestToken: String) {
    verify(mock).invoke(requestToken)
  }

  fun verifyNoSessionInteraction() {
    verifyNoInteractions(mock)
  }
}
