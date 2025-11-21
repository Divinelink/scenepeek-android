package com.divinelink.core.domain.session

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.data.auth.AuthRepository
import com.divinelink.core.data.session.repository.SessionRepository
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.datastore.auth.accessToken
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.exception.SessionException

class LogoutUseCase(
  private val repository: SessionRepository,
  private val authRepository: AuthRepository,
  private val sessionStorage: SessionStorage,
  val dispatcher: DispatcherProvider,
) : UseCase<Unit, Unit>(dispatcher.default) {

  override suspend fun execute(parameters: Unit) {
    val accessToken: String = sessionStorage.savedState.accessToken
      ?: throw SessionException.Unauthenticated()

    repository
      .deleteSession(accessToken)
      .onSuccess {
        authRepository.clearTMDBSession()
      }
      .onFailure {
        if (it is AppException.Unauthorized) {
          authRepository.clearTMDBSession()
        } else {
          throw it
        }
      }
  }
}
