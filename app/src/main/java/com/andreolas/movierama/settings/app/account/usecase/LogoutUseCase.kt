package com.andreolas.movierama.settings.app.account.usecase

import com.andreolas.movierama.base.di.IoDispatcher
import com.andreolas.movierama.session.SessionStorage
import gr.divinelink.core.util.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
  private val sessionStorage: SessionStorage,
  @IoDispatcher val dispatcher: CoroutineDispatcher
) : UseCase<Unit, Unit>(dispatcher) {

  override suspend fun execute(parameters: Unit) {
    sessionStorage.clearSession()
  }
}
