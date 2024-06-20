package com.divinelink.core.domain

import com.divinelink.core.commons.di.IoDispatcher
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.domain.session.CreateSessionUseCase
import com.divinelink.core.model.session.tokenIsApproved
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Use case that handles the authentication request.
 * It checks if the request was approved or denied and updates the token status accordingly.
 * If the request was approved, it creates a new session with the given token.
 *
 * In any case, it clears the token from the session storage when the request is either
 * approved or denied.
 */
class HandleAuthenticationRequestUseCase @Inject constructor(
  private val storage: PreferenceStorage,
  private val sessionStorage: SessionStorage,
  private val createSessionUseCase: CreateSessionUseCase,
  @IoDispatcher val dispatcher: CoroutineDispatcher,
) : UseCase<String, Unit>(dispatcher) {

  override suspend fun execute(parameters: String) {
    val token = storage.token.first() ?: return

    if (parameters.tokenIsApproved) {
      // Request was approved by the user
      createSessionUseCase.invoke(token).onSuccess {
        sessionStorage.clearToken()
      }
    } else {
      // Request was denied by the user
      sessionStorage.clearToken()
    }
  }
}
