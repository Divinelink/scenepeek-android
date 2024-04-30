package com.andreolas.movierama.settings.app.account.usecase

import com.andreolas.movierama.base.di.IoDispatcher
import com.andreolas.movierama.base.storage.EncryptedStorage
import com.andreolas.movierama.session.model.AccountDetails
import com.andreolas.movierama.session.repository.SessionRepository
import gr.divinelink.core.util.domain.FlowUseCase
import gr.divinelink.core.util.domain.data
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAccountDetailsUseCase @Inject constructor(
  private val repository: SessionRepository,
  private val encryptedStorage: EncryptedStorage,
  @IoDispatcher val dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, AccountDetails>(dispatcher) {

  override fun execute(parameters: Unit): Flow<Result<AccountDetails>> = flow {
    val sessionId = encryptedStorage.sessionId

    if (sessionId == null) {
      emit(Result.failure(Exception("Missing token ID")))
      return@flow
    }

    val response = repository.getAccountDetails(sessionId)
    emit(Result.success(response.first().data))
  }
}
