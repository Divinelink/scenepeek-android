package com.divinelink.core.domain.session

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.data.auth.AuthRepository
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.model.account.TMDBAccount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class ObserveAccountUseCase(
  private val authRepository: AuthRepository,
  private val sessionStorage: SessionStorage,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<Unit, TMDBAccount>(dispatcher.default) {

  override fun execute(parameters: Unit): Flow<Result<TMDBAccount>> = combine(
    sessionStorage.sessionFlow,
    authRepository.tmdbAccount,
  ) { session, account ->
    when {
      session == null -> TMDBAccount.Anonymous
      account == null -> TMDBAccount.Loading
      else -> TMDBAccount.LoggedIn(account)
    }
  }.map { Result.success(it) }
}
