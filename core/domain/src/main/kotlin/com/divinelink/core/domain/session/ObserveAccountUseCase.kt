package com.divinelink.core.domain.session

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.data.auth.AuthRepository
import com.divinelink.core.model.exception.SessionException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ObserveAccountUseCase(
  private val authRepository: AuthRepository,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<Unit, Boolean>(dispatcher.default) {

  override fun execute(parameters: Unit): Flow<Result<Boolean>> = flow {
    authRepository.tmdbAccount.collect { account ->
      if (account == null) {
        emit(Result.failure(SessionException.Unauthenticated()))
      } else {
        emit(Result.success(true))
      }
    }
  }
}
