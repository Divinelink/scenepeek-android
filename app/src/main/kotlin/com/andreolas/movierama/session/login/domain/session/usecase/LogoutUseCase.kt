package com.andreolas.movierama.session.login.domain.session.usecase

import com.andreolas.movierama.base.di.IoDispatcher
import com.andreolas.movierama.session.SessionStorage
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.data.session.repository.SessionRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
  private val repository: SessionRepository,
  private val sessionStorage: SessionStorage,
  @IoDispatcher val dispatcher: CoroutineDispatcher
) : UseCase<Unit, Unit>(dispatcher) {

  override suspend fun execute(parameters: Unit) {
    val sessionId: String = sessionStorage.sessionId ?: throw IllegalStateException(
      "No session id found."
    )

    val deleteResult = repository.deleteSession(sessionId)

    deleteResult.onSuccess {
      sessionStorage.clearSession()
    }
  }
}
