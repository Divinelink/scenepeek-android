package com.divinelink.core.domain.session

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.session.repository.SessionRepository
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.network.session.model.CreateSessionRequestApi

class CreateSessionUseCase(
  private val repository: SessionRepository,
  private val sessionStorage: SessionStorage,
  val dispatcher: DispatcherProvider,
) : UseCase<String, Unit>(dispatcher.io) {

  override suspend fun execute(parameters: String) {
    val result = repository.createSession(CreateSessionRequestApi(parameters))

    sessionStorage.setSession(result.data.id)
  }
}
