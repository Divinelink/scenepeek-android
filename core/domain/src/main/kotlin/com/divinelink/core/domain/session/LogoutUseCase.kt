package com.divinelink.core.domain.session

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.data.auth.AuthRepository
import com.divinelink.core.data.session.repository.SessionRepository
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.exception.SessionException

class LogoutUseCase(
  private val repository: SessionRepository,
  private val authRepository: AuthRepository,
  private val sessionStorage: SessionStorage,
  val dispatcher: DispatcherProvider,
) : UseCase<Unit, Unit>(dispatcher.default) {

  override suspend fun execute(parameters: Unit) {
    val accessToken: String = sessionStorage.encryptedStorage.accessToken
      ?: throw SessionException.Unauthenticated()

    repository
      .deleteSession(accessToken)
      .onSuccess {
        authRepository.clearTMDBAccount()
        sessionStorage.clearSession()
      }
      .onFailure {
        if (it is AppException.Unauthorized) {
          sessionStorage.clearSession()
          authRepository.clearTMDBAccount()
        } else {
          throw it
        }
      }
  }
}
