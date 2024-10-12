package com.divinelink.core.domain.session

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.data.session.model.SessionException
import com.divinelink.core.datastore.PreferenceStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ObserveSessionUseCase(
  private val storage: PreferenceStorage,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<Unit, Boolean>(dispatcher.io) {

  override fun execute(parameters: Unit): Flow<Result<Boolean>> = flow {
    storage.hasSession.collect { hasSession ->
      if (hasSession) {
        emit(Result.success(true))
      } else {
        emit(Result.failure(SessionException.Unauthenticated()))
      }
    }
  }
}
