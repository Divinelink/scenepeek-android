package com.divinelink.core.domain

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.data.auth.AuthRepository
import com.divinelink.core.data.session.repository.SessionRepository
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.model.account.TMDBAccount
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.exception.SessionException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class GetAccountDetailsUseCase(
  private val repository: SessionRepository,
  private val authRepository: AuthRepository,
  private val storage: SessionStorage,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<Unit, TMDBAccount>(dispatcher.default) {

  override fun execute(parameters: Unit): Flow<Result<TMDBAccount>> = channelFlow {
    launch(dispatcher.default) {
      authRepository
        .tmdbAccount
        .distinctUntilChanged()
        .collect { accountDetails ->
          if (accountDetails == null) {
            send(Result.failure(SessionException.Unauthenticated()))
            authRepository.clearTMDBSession()
          } else {
            send(Result.success(TMDBAccount.LoggedIn(accountDetails)))
          }
        }
    }

    launch(dispatcher.default) {
      storage.sessionFlow.collect { session ->
        send(Result.success(TMDBAccount.Loading))
        if (session == null) {
          send(Result.failure(SessionException.Unauthenticated()))
          authRepository.clearTMDBSession()
        } else {
          repository.getAccountDetails(session.sessionId).fold(
            onSuccess = { details ->
              authRepository.setTMDBAccount(details)
              send(Result.success(TMDBAccount.LoggedIn(details)))
            },
            onFailure = {
              if (it is AppException.Unauthorized) {
                authRepository.clearTMDBSession()
                send(Result.failure(SessionException.Unauthenticated()))
              } else {
                send(Result.failure(it))
              }
            },
          )
        }
      }
    }
  }
}
