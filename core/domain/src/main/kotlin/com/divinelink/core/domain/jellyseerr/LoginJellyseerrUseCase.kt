package com.divinelink.core.domain.jellyseerr

import com.divinelink.core.commons.di.IoDispatcher
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.data.jellyseerr.JellyseerrRepository
import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.model.jellyseerr.JellyfinLogin
import com.divinelink.core.model.jellyseerr.JellyseerrLoginMethod
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import javax.inject.Inject

open class LoginJellyseerrUseCase @Inject constructor(
  private val repository: JellyseerrRepository,
  private val storage: PreferenceStorage,
  @IoDispatcher val dispatcher: CoroutineDispatcher,
) : FlowUseCase<JellyfinLogin, Unit>(dispatcher) {

  override fun execute(parameters: JellyfinLogin): Flow<Result<Unit>> = flow {
    repository.signInWithJellyfin(parameters)
      .last()
      .fold(
        onSuccess = {
          storage.setJellyseerrAccount(parameters.username.value)
          storage.setJellyseerrAddress(parameters.address)
          storage.setJellyseerrSignInMethod(JellyseerrLoginMethod.JELLYFIN.name)
          emit(Result.success(Unit))
        },
        onFailure = {
          emit(Result.failure(it))
        },
      )
  }
}
