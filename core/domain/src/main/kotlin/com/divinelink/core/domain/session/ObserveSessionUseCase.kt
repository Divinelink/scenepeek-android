package com.divinelink.core.domain.session

import com.divinelink.core.commons.di.IoDispatcher
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.data.session.model.SessionException
import com.divinelink.core.datastore.PreferenceStorage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ObserveSessionUseCase @Inject constructor(
  private val storage: PreferenceStorage,
  @IoDispatcher val dispatcher: CoroutineDispatcher,
) : FlowUseCase<Unit, Boolean>(dispatcher) {

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
