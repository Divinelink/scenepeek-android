package com.andreolas.movierama.session.login.domain.session.usecase

import com.divinelink.core.commons.di.IoDispatcher
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.session.repository.SessionRepository
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.network.session.model.CreateSessionRequestApi
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class CreateSessionUseCase @Inject constructor(
  private val repository: SessionRepository,
  private val sessionStorage: SessionStorage,
  @IoDispatcher val dispatcher: CoroutineDispatcher
) : UseCase<String, Unit>(dispatcher) {

  override suspend fun execute(parameters: String) {
    val result = repository.createSession(CreateSessionRequestApi(parameters))

    sessionStorage.setSession(result.data.id)
  }
}
