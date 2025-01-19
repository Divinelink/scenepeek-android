package com.divinelink.core.domain.session

import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.model.session.Session
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.repository.TestSessionRepository
import com.divinelink.core.testing.storage.FakeEncryptedPreferenceStorage
import com.divinelink.core.testing.storage.FakePreferenceStorage
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test

class CreateSessionUseCaseTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  @Test
  fun `test on create session with success saves session id to storage`() = runTest {
    val repository = TestSessionRepository()
    val storage = SessionStorage(
      storage = FakePreferenceStorage(),
      encryptedStorage = FakeEncryptedPreferenceStorage(),
    )

    repository.mockCreateSession(
      response = Result.success(Session(id = "sessionId")),
    )

    val useCase = CreateSessionUseCase(
      repository = repository.mock,
      storage = storage,
      dispatcher = testDispatcher,
    )

    assertThat(storage.sessionId).isNull()

    useCase("requestToken")

    assertThat(storage.sessionId).isEqualTo("sessionId")
  }

  @Test
  fun `test on create session with failure does not save session to storage`() = runTest {
    val repository = TestSessionRepository()
    val storage = SessionStorage(
      storage = FakePreferenceStorage(),
      encryptedStorage = FakeEncryptedPreferenceStorage(),
    )

    repository.mockCreateSession(
      response = Result.failure(Exception()),
    )

    val useCase = CreateSessionUseCase(
      repository = repository.mock,
      storage = storage,
      dispatcher = testDispatcher,
    )

    assertThat(storage.sessionId).isNull()

    useCase("requestToken")

    assertThat(storage.sessionId).isNull()
  }
}
