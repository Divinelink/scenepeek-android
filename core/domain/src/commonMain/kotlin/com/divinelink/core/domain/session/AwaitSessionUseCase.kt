package com.divinelink.core.domain.session

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.datastore.SessionStorage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeoutOrNull

private const val SESSION_TIMEOUT = 10_000L

/**
 * Suspends until the active TMDB session changes (or first appears), with a timeout
 * fallback. Used by callers that need to wait for [CreateSessionUseCase] to finish
 * when it's been triggered from elsewhere - for example the auth deeplink handler.
 */
class AwaitSessionUseCase(
  private val sessionStorage: SessionStorage,
  val dispatcher: DispatcherProvider,
) : UseCase<Unit, Unit>(dispatcher.default) {

  override suspend fun execute(parameters: Unit) {
    val initialSessionId = sessionStorage.sessionId
    withTimeoutOrNull(SESSION_TIMEOUT) {
      sessionStorage.sessionFlow.first { it?.sessionId != initialSessionId }
    }
  }
}
