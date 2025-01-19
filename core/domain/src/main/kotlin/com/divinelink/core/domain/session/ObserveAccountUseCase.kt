package com.divinelink.core.domain.session

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.data.session.model.SessionException
import com.divinelink.core.datastore.PreferenceStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class ObserveAccountUseCase(
  private val storage: PreferenceStorage,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<Unit, Boolean>(dispatcher.io) {

  override fun execute(parameters: Unit): Flow<Result<Boolean>> = combine(
    storage.hasSession,
    storage.accountId,
  ) { hasSession, accountId ->
    if (hasSession && accountId != null) {
      Result.success(true)
    } else {
      Result.failure(SessionException.Unauthenticated())
    }
  }
}
