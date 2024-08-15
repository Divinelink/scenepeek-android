package com.divinelink.core.domain.jellyseerr


import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.data.jellyseerr.repository.JellyseerrRepository
import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.model.jellyseerr.JellyseerrAccountDetails
import com.divinelink.core.commons.domain.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

open class GetJellyseerrDetailsUseCase(
  private val storage: PreferenceStorage,
  private val repository: JellyseerrRepository,
  val dispatcher: DispatcherProvider
) : FlowUseCase<Unit, JellyseerrAccountDetails?>(dispatcher.io) {

  override fun execute(parameters: Unit): Flow<Result<JellyseerrAccountDetails?>> = combine(
    storage.jellyseerrAccount,
    storage.jellyseerrAddress,
    storage.jellyseerrSignInMethod,
    repository.getJellyseerrAccountDetails(),
  ) { account, address, signInMethod, jellyseerrDetails ->
    when {
      account == null && address == null && signInMethod == null -> {
        Result.failure(Exception("No account found."))
      }
      else -> {
        Result.success(jellyseerrDetails)
      }
    }
  }
}
