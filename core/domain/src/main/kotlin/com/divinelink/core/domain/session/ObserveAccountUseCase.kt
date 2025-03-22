package com.divinelink.core.domain.session

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.data.session.model.SessionException
import com.divinelink.core.datastore.account.AccountStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ObserveAccountUseCase(
  private val storage: AccountStorage,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<Unit, Boolean>(dispatcher.io) {

  override fun execute(parameters: Unit): Flow<Result<Boolean>> = flow {
    storage.accountId.collect { accountId ->
      if (accountId == null) {
        emit(Result.failure(SessionException.Unauthenticated()))
      } else {
        emit(Result.success(true))
      }
    }
  }
}
