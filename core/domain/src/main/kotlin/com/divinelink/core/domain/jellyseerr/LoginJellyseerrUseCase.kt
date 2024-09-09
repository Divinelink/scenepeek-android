package com.divinelink.core.domain.jellyseerr

import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.data.jellyseerr.repository.JellyseerrRepository
import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.model.jellyseerr.JellyseerrAccountDetails
import com.divinelink.core.model.jellyseerr.JellyseerrLoginMethod
import com.divinelink.core.model.jellyseerr.JellyseerrLoginParams
import com.divinelink.core.commons.domain.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last

open class LoginJellyseerrUseCase(
  private val repository: JellyseerrRepository,
  private val storage: PreferenceStorage,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<JellyseerrLoginParams?, JellyseerrAccountDetails>(dispatcher.io) {

  override fun execute(parameters: JellyseerrLoginParams?): Flow<Result<JellyseerrAccountDetails>> =
    flow {
      if (parameters == null) {
        emit(Result.failure(IllegalArgumentException("Parameters cannot be null")))
        return@flow
      }

      val result = when (parameters.signInMethod) {
        JellyseerrLoginMethod.JELLYFIN -> repository.signInWithJellyfin(
          parameters.toLoginData(),
        )
        JellyseerrLoginMethod.JELLYSEERR -> repository.signInWithJellyseerr(
          parameters.toLoginData(),
        )
      }

      result.last().fold(
        onSuccess = { accountDetails ->
          storage.setJellyseerrAccount(parameters.username.value)
          storage.setJellyseerrAddress(parameters.address)
          storage.setJellyseerrSignInMethod(parameters.signInMethod.name)
          repository.insertJellyseerrAccountDetails(accountDetails)

          emit(Result.success(accountDetails))
        },
        onFailure = {
          emit(Result.failure(it))
        },
      )
    }
}
