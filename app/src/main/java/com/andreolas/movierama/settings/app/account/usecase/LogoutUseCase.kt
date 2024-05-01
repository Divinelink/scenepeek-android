package com.andreolas.movierama.settings.app.account.usecase

import com.andreolas.movierama.base.di.IoDispatcher
import com.andreolas.movierama.session.SessionStorage
import com.andreolas.movierama.session.repository.SessionRepository
import gr.divinelink.core.util.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
  private val repository: SessionRepository,
  private val sessionStorage: SessionStorage,
  @IoDispatcher val dispatcher: CoroutineDispatcher
) : UseCase<Unit, Unit>(dispatcher) {

  override suspend fun execute(parameters: Unit) {
    val sessionId: String = sessionStorage.sessionId
      ?: throw IllegalStateException("No session id found.")

    val deleteResult = repository.deleteSession(sessionId)

    deleteResult.onSuccess {
      sessionStorage.clearSession()
    }
  }
}
