package com.divinelink.core.domain.session

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.data.auth.AuthRepository
import com.divinelink.core.data.session.repository.SessionRepository
import com.divinelink.core.datastore.SessionStorage
import kotlinx.coroutines.delay

const val TMDB_AUTH_DELAY = 1500L // 1.5 seconds

class CreateSessionUseCase(
  private val repository: SessionRepository,
  private val authRepository: AuthRepository,
  private val storage: SessionStorage,
  val dispatcher: DispatcherProvider,
) : UseCase<Unit, Unit>(dispatcher.default) {

  override suspend fun execute(parameters: Unit) {
    val requestToken = repository.retrieveRequestToken().getOrNull()

    if (requestToken?.token == null) {
      authRepository.clearTMDBSession()
      repository.clearRequestToken()
      return
    }

    repository.createAccessToken(requestToken.token).fold(
      onSuccess = { accessToken ->
        repository.createSession(accessToken = accessToken.accessToken).fold(
          onSuccess = { session ->
            storage.setAccessToken(
              sessionId = session.id,
              accessToken = accessToken,
            )

            // There's a delay to make sure the session is properly created before proceeding
            delay(TMDB_AUTH_DELAY)

            repository.clearRequestToken()
          },
          onFailure = {
            authRepository.clearTMDBSession()
            repository.clearRequestToken()
          },
        )
      },
      onFailure = {
        authRepository.clearTMDBSession()
        repository.clearRequestToken()
      },
    )
  }
}
