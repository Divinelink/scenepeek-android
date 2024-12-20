package com.divinelink.core.domain.session

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.data.session.repository.SessionRepository
import com.divinelink.core.datastore.SessionStorage

class LogoutUseCase(
  private val repository: SessionRepository,
  private val sessionStorage: SessionStorage,
  val dispatcher: DispatcherProvider,
) : UseCase<Unit, Unit>(dispatcher.io) {

  override suspend fun execute(parameters: Unit) {
    val sessionId: String = sessionStorage.sessionId ?: throw IllegalStateException(
      "No session id found.",
    )

    val deleteResult = repository.deleteSession(sessionId)

    deleteResult.onSuccess {
      sessionStorage.clearSession()
    }
  }
}
