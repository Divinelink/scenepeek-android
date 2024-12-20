package com.divinelink.core.domain

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.session.model.SessionException
import com.divinelink.core.data.session.repository.SessionRepository
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.model.account.AccountDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class GetAccountDetailsUseCase(
  private val repository: SessionRepository,
  private val sessionStorage: SessionStorage,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<Unit, AccountDetails>(dispatcher.io) {

  override fun execute(parameters: Unit): Flow<Result<AccountDetails>> = flow {
    val sessionId = sessionStorage.sessionId

    if (sessionId == null) {
      emit(Result.failure(SessionException.Unauthenticated()))
      return@flow
    }

    val response = repository.getAccountDetails(sessionId)
    val data = response.first().data

    sessionStorage.setAccountId(data.id.toString())

    emit(Result.success(data))
  }
}
