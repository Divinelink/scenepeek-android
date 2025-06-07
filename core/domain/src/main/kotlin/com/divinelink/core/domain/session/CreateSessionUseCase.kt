package com.divinelink.core.domain.session

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.session.repository.SessionRepository
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.network.session.model.CreateSessionRequestApi
import kotlinx.coroutines.delay

const val TMDB_AUTH_DELAY = 3000L // 3 seconds

class CreateSessionUseCase(
  private val repository: SessionRepository,
  private val storage: SessionStorage,
  val dispatcher: DispatcherProvider,
) : UseCase<String, Unit>(dispatcher.io) {

  // @param parameters: requestToken
  override suspend fun execute(parameters: String) {
    repository
      .createSession(CreateSessionRequestApi(parameters))
      .onSuccess {
        // There's a delay to make sure the session is properly created before proceeding
        delay(TMDB_AUTH_DELAY)
        storage.setSession(it.id)

        // Fetch account details
        repository.getAccountDetails(it.id).collect { accountDetailsResult ->
          storage.setTMDbAccountDetails(accountDetailsResult.data)
        }
      }
      .onFailure {
        storage.clearSession()
      }
  }
}
