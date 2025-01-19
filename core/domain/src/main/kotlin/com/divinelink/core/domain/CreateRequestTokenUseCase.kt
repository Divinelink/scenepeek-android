package com.divinelink.core.domain

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.session.repository.SessionRepository
import timber.log.Timber

class CreateRequestTokenUseCase(
  private val repository: SessionRepository,
  val dispatcher: DispatcherProvider,
) : UseCase<Unit, String>(dispatcher.io) {

  override suspend fun execute(parameters: Unit): String {
    Timber.d("Creating new token")
    val result = repository
      .createRequestToken()
      .onSuccess { result ->
        Timber.d("Token created successfully")

        result.token
      }

    return result.data.token
  }
}
