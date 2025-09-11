package com.divinelink.core.domain.jellyseerr

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.data.auth.AuthRepository
import com.divinelink.core.data.jellyseerr.repository.JellyseerrRepository
import com.divinelink.core.datastore.auth.SavedState
import com.divinelink.core.model.jellyseerr.JellyseerrAuthMethod
import com.divinelink.core.model.jellyseerr.JellyseerrLoginData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map

class LoginJellyseerrUseCase(
  private val repository: JellyseerrRepository,
  private val authRepository: AuthRepository,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<JellyseerrLoginData, Unit>(dispatcher.default) {

  override fun execute(parameters: JellyseerrLoginData): Flow<Result<Unit>> = flow {
    val result = when (parameters.authMethod) {
      JellyseerrAuthMethod.JELLYFIN,
      JellyseerrAuthMethod.EMBY,
      -> repository.signInWithJellyfin(parameters)
      JellyseerrAuthMethod.JELLYSEERR -> repository.signInWithJellyseerr(parameters)
    }

    result.last().fold(
      onSuccess = {
        val details = repository.getRemoteAccountDetails(parameters.address).first().getOrThrow()

        authRepository.updateJellyseerrAccount(
          SavedState.JellyseerrAccount(
            account = parameters.username.value,
            address = parameters.address,
            authMethod = parameters.authMethod,
            password = parameters.password.value,
          ),
        )

        repository.insertJellyseerrAccountDetails(details)

        emit(Result.success(Unit))
      },
      onFailure = {
        emit(Result.failure(it))
      },
    )
  }
}
