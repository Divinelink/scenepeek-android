package com.andreolas.movierama.session.login.domain.session.usecase

import com.andreolas.movierama.base.di.IoDispatcher
import com.andreolas.movierama.base.storage.PreferenceStorage
import com.divinelink.core.commons.domain.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ObserveSessionUseCase @Inject constructor(
  private val storage: PreferenceStorage,
  @IoDispatcher val dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, Boolean>(dispatcher) {

  override fun execute(parameters: Unit): Flow<Result<Boolean>> = flow {
    storage.hasSession.collect { hasSession ->
      if (hasSession) {
        emit(Result.success(true))
      } else {
        emit(Result.failure(IllegalStateException("User does not have session")))
      }
    }
  }
}
