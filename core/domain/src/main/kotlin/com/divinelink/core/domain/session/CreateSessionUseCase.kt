package com.divinelink.core.domain.session

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.data.session.repository.SessionRepository
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.network.session.model.CreateSessionRequestApi

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
        storage.setSession(it.id)
      }
      .onFailure {
        // Do nothing
      }
  }
}
