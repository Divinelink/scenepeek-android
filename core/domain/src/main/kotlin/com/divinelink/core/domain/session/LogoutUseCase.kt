package com.divinelink.core.domain.session

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.commons.exception.InvalidStatusException
import com.divinelink.core.model.exception.SessionException
import com.divinelink.core.data.session.repository.SessionRepository
import com.divinelink.core.datastore.SessionStorage

class LogoutUseCase(
  private val repository: SessionRepository,
  private val sessionStorage: SessionStorage,
  val dispatcher: DispatcherProvider,
) : UseCase<Unit, Unit>(dispatcher.default) {

  override suspend fun execute(parameters: Unit) {
    val accessToken: String = sessionStorage.encryptedStorage.accessToken
      ?: throw SessionException.Unauthenticated()

    repository
      .deleteSession(accessToken)
      .onSuccess {
        sessionStorage.clearSession()
      }
      .onFailure {
        if (it is InvalidStatusException && it.status == 401) {
          sessionStorage.clearSession()
        } else {
          throw it
        }
      }
  }
}
