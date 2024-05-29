package com.andreolas.movierama.settings.app.account.usecase

import com.andreolas.movierama.base.di.IoDispatcher
import com.andreolas.movierama.session.SessionStorage
import com.divinelink.core.data.session.model.SessionException
import com.divinelink.core.data.session.repository.SessionRepository
import com.divinelink.core.model.account.AccountDetails
import gr.divinelink.core.util.domain.FlowUseCase
import gr.divinelink.core.util.domain.data
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAccountDetailsUseCase @Inject constructor(
  private val repository: SessionRepository,
  private val sessionStorage: SessionStorage,
  @IoDispatcher val dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, AccountDetails>(dispatcher) {

  override fun execute(parameters: Unit): Flow<Result<AccountDetails>> = flow {
    val sessionId = sessionStorage.sessionId

    if (sessionId == null) {
      emit(Result.failure(SessionException.NoSession()))
      return@flow
    }

    val response = repository.getAccountDetails(sessionId)
    val data = response.first().data

    sessionStorage.setAccountId(data.id.toString())

    emit(Result.success(data))
  }
}
