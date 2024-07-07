package com.divinelink.core.domain.jellyseerr

import com.divinelink.core.commons.di.IoDispatcher
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.model.jellyseerr.JellyseerrAccountDetails
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

open class GetJellyseerrDetailsUseCase @Inject constructor(
  private val storage: PreferenceStorage,
  @IoDispatcher val dispatcher: CoroutineDispatcher,
) : FlowUseCase<Unit, JellyseerrAccountDetails>(dispatcher) {

  override fun execute(parameters: Unit): Flow<Result<JellyseerrAccountDetails>> = combine(
    storage.jellyseerrAccount,
    storage.jellyseerrAddress,
    storage.jellyseerrSignInMethod,
  ) { account, address, signInMethod ->
    when {
      account == null -> {
        Result.failure(Exception("No account found."))
      }
      address == null -> {
        Result.failure(Exception("No address found."))
      }
      signInMethod == null -> {
        Result.failure(Exception("No sign in method found."))
      }
      else -> {
        Result.success(
          JellyseerrAccountDetails(
            displayName = account,
            avatar = "",
            requestCount = 0,
          ),
        )
      }
    }
  }
}
