package com.divinelink.core.domain

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.session.model.SessionException
import com.divinelink.core.data.session.repository.SessionRepository
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.model.account.AccountDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber

class GetAccountDetailsUseCase(
  private val repository: SessionRepository,
  private val storage: SessionStorage,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<Unit, AccountDetails>(dispatcher.default) {

  override fun execute(parameters: Unit): Flow<Result<AccountDetails>> = channelFlow {
    val sessionId = storage.sessionId

    launch(dispatcher.default) {
      storage.accountStorage.accountDetails.collect { accountDetails ->
        Timber.i("Details updated: $accountDetails")
        if (accountDetails != null) {
          send(Result.success(accountDetails))
        }
      }
    }

    launch(dispatcher.default) {
      if (sessionId == null) {
        send(Result.failure(SessionException.Unauthenticated()))
      } else {
        val details = repository.getAccountDetails(sessionId).first().data
        storage.accountStorage.setAccountDetails(details)
      }
    }
  }
}
