package com.divinelink.core.domain

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.commons.exception.InvalidStatusException
import com.divinelink.core.model.exception.SessionException
import com.divinelink.core.data.session.repository.SessionRepository
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.model.account.TMDBAccount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class GetAccountDetailsUseCase(
  private val repository: SessionRepository,
  private val storage: SessionStorage,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<Unit, TMDBAccount>(dispatcher.default) {

  override fun execute(parameters: Unit): Flow<Result<TMDBAccount>> = channelFlow {
    val sessionId = storage.sessionId

    launch(dispatcher.default) {
      storage.accountStorage.accountDetails.collect { accountDetails ->
        Timber.i("Details updated: $accountDetails")
        if (accountDetails == null) {
          send(Result.failure(SessionException.Unauthenticated()))
        } else {
          send(Result.success(TMDBAccount.LoggedIn(accountDetails)))
        }
      }
    }

    launch(dispatcher.default) {
      if (sessionId == null) {
        send(Result.failure(SessionException.Unauthenticated()))
      } else {
        repository.getAccountDetails(sessionId).fold(
          onSuccess = { details ->
            storage.accountStorage.setAccountDetails(details)
          },
          onFailure = {
            if (it is InvalidStatusException && it.status == 401) {
              storage.clearSession()
              send(Result.failure(SessionException.Unauthenticated()))
            } else {
              send(Result.failure(it))
            }
          },
        )
//          .collect { result ->
//          result.fold(
//            onSuccess = { details ->
//
//            },
//            onFailure = {
//
//            },
//          )
//      }
      }
    }
  }
}
