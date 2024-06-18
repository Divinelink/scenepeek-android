package com.divinelink.core.domain

import com.divinelink.core.commons.di.IoDispatcher
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.session.repository.SessionRepository
import com.divinelink.core.datastore.PreferenceStorage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

open class CreateRequestTokenUseCase @Inject constructor(
  private val storage: PreferenceStorage,
  private val repository: SessionRepository,
  @IoDispatcher val dispatcher: CoroutineDispatcher
) : UseCase<Unit, String>(dispatcher) {

  override suspend fun execute(parameters: Unit): String {
    val token = storage.token.first()

    if (token != null) {
      if (token.isNotBlank()) {
        Timber.d("Token already exists in storage")
        return token
      }
    }

    Timber.d("Creating new token")
    val result = repository.createRequestToken()
      .onSuccess { result ->
        Timber.d("Token created successfully")
        storage.setToken(token = result.token)

        result.token
      }

    return result.data.token
  }
}
