package com.divinelink.core.domain.session

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.data.auth.AuthRepository
import com.divinelink.core.data.session.repository.SessionRepository
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.datastore.auth.accessToken

class LogoutUseCase(
  private val repository: SessionRepository,
  private val authRepository: AuthRepository,
  private val sessionStorage: SessionStorage,
  val dispatcher: DispatcherProvider,
) : UseCase<Unit, Unit>(dispatcher.default) {

  override suspend fun execute(parameters: Unit) {
    val accessToken: String? = sessionStorage.savedState.accessToken

    if (accessToken == null) {
      authRepository.clearTMDBSession()
    } else {
      repository
        .deleteSession(accessToken)
        .onSuccess {
          authRepository.clearTMDBSession()
        }
        .onFailure {
          authRepository.clearTMDBSession()
        }
    }
  }
}
