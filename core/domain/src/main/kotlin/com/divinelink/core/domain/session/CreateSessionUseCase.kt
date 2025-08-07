package com.divinelink.core.domain.session

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.data.session.repository.SessionRepository
import com.divinelink.core.datastore.SessionStorage
import kotlinx.coroutines.delay

const val TMDB_AUTH_DELAY = 1500L // 3 seconds

class CreateSessionUseCase(
  private val repository: SessionRepository,
  private val storage: SessionStorage,
  val dispatcher: DispatcherProvider,
) : UseCase<Unit, Unit>(dispatcher.default) {

  override suspend fun execute(parameters: Unit) {
    val requestToken = repository.retrieveRequestToken().getOrNull()

    if (requestToken?.token == null) {
      storage.clearSession()
      repository.clearRequestToken()
      return
    }

    repository.createAccessToken(requestToken.token).fold(
      onSuccess = { accessToken ->
        repository.createSession(accessToken = accessToken.accessToken).fold(
          onSuccess = { session ->
            // There's a delay to make sure the session is properly created before proceeding
            delay(TMDB_AUTH_DELAY)

            storage.setAccessToken(
              sessionId = session.id,
              accessToken = accessToken,
            )

            repository.getAccountDetails(session.id).onSuccess { accountDetails ->
              storage.setTMDbAccountDetails(accountDetails)
            }

            repository.clearRequestToken()
          },
          onFailure = {
            storage.clearSession()
            repository.clearRequestToken()
          },
        )
      },
      onFailure = {
        storage.clearSession()
        repository.clearRequestToken()
      },
    )
  }
}
