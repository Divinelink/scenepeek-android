package com.divinelink.core.domain.jellyseerr

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.data.jellyseerr.repository.JellyseerrRepository
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.model.jellyseerr.JellyseerrAccountDetails
import com.divinelink.core.model.jellyseerr.JellyseerrAuthMethod
import com.divinelink.core.model.jellyseerr.JellyseerrLoginData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last

open class LoginJellyseerrUseCase(
  private val repository: JellyseerrRepository,
  private val storage: SessionStorage,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<JellyseerrLoginData, JellyseerrAccountDetails>(dispatcher.default) {

  override fun execute(parameters: JellyseerrLoginData): Flow<Result<JellyseerrAccountDetails>> =
    flow {
      val result = when (parameters.authMethod) {
        JellyseerrAuthMethod.JELLYFIN,
        JellyseerrAuthMethod.EMBY,
        -> repository.signInWithJellyfin(parameters)
        JellyseerrAuthMethod.JELLYSEERR -> repository.signInWithJellyseerr(parameters)
      }

      result.last().fold(
        onSuccess = {
          val details = repository.getRemoteAccountDetails(parameters.address).first().getOrThrow()

          storage.setJellyseerrSession(
            username = parameters.username.value,
            address = parameters.address,
            authMethod = parameters.authMethod.name,
            password = parameters.password.value,
          )
          repository.insertJellyseerrAccountDetails(details)

          emit(Result.success(details))
        },
        onFailure = {
          emit(Result.failure(it))
        },
      )
    }
}
