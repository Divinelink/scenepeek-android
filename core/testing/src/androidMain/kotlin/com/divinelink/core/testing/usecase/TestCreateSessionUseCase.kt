package com.divinelink.core.testing.usecase

import com.divinelink.core.domain.session.CreateSessionUseCase
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.factories.storage.SessionStorageFactory
import com.divinelink.core.testing.repository.TestAuthRepository
import com.divinelink.core.testing.repository.TestSessionRepository
import org.junit.Rule
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever

class TestCreateSessionUseCase {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  val mock: CreateSessionUseCase = mock()

  fun useCase() = CreateSessionUseCase(
    repository = TestSessionRepository().mock,
    authRepository = TestAuthRepository().mock,
    storage = SessionStorageFactory.empty(),
    dispatcher = mainDispatcherRule.testDispatcher,
  )

  suspend fun mockResponse(response: Result<Unit>) {
    whenever(mock.invoke(Unit)).thenReturn(response)
  }

  suspend fun verifySessionInvoked() {
    verify(mock).invoke(Unit)
  }

  fun verifyNoSessionInteraction() {
    verifyNoInteractions(mock)
  }
}
