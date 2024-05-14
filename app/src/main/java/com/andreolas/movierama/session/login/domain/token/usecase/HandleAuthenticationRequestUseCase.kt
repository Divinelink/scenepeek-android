package com.andreolas.movierama.session.login.domain.token.usecase

import com.andreolas.movierama.base.di.IoDispatcher
import com.andreolas.movierama.base.storage.PreferenceStorage
import com.andreolas.movierama.session.SessionStorage
import com.andreolas.movierama.session.login.domain.session.usecase.CreateSessionUseCase
import com.divinelink.core.model.session.tokenIsApproved
import gr.divinelink.core.util.domain.UseCase
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
  @IoDispatcher val dispatcher: CoroutineDispatcher
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
