package com.divinelink.core.domain

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.session.repository.SessionRepository
import com.divinelink.core.domain.session.TMDB_AUTH_DELAY
import kotlinx.coroutines.delay
import timber.log.Timber

class CreateRequestTokenUseCase(
  private val repository: SessionRepository,
  val dispatcher: DispatcherProvider,
) : UseCase<Unit, String>(dispatcher.default) {

  override suspend fun execute(parameters: Unit): String {
    Timber.d("Creating new token")
    val result = repository
      .createRequestToken()
      .onSuccess { result ->
        Timber.d("Token created successfully")

        result.token
      }

    // Wait for 3 seconds to ensure the token is created properly
    delay(TMDB_AUTH_DELAY)

    return result.data.token
  }
}
