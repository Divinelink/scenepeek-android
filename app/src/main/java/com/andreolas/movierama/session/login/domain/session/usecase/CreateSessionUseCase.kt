package com.andreolas.movierama.session.login.domain.session.usecase

import com.andreolas.movierama.base.di.IoDispatcher
import com.andreolas.movierama.session.SessionStorage
import com.divinelink.core.data.session.repository.SessionRepository
import com.divinelink.core.network.session.model.CreateSessionRequestApi
import gr.divinelink.core.util.domain.UseCase
import gr.divinelink.core.util.domain.data
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
