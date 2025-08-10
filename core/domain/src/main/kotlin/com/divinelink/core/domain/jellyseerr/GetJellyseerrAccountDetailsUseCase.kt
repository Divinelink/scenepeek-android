package com.divinelink.core.domain.jellyseerr

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.data.jellyseerr.repository.JellyseerrRepository
import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.model.jellyseerr.JellyseerrAccountDetails
import com.divinelink.core.network.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.distinctUntilChanged

data class JellyseerrAccountDetailsResult(
  val address: String,
  val accountDetails: JellyseerrAccountDetails?,
)

class GetJellyseerrAccountDetailsUseCase(
  private val storage: PreferenceStorage,
  private val repository: JellyseerrRepository,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<Boolean, JellyseerrAccountDetailsResult>(dispatcher.default) {

  /**
   * @param parameters: If true, fetch from remote
   */
  override fun execute(parameters: Boolean): Flow<Result<JellyseerrAccountDetailsResult>> =
    channelFlow {
      storage
        .jellyseerrAddress
        .distinctUntilChanged()
        .collect { address ->
          if (address != null) {
            repository.getJellyseerrAccountDetails(
              address = address,
              refresh = parameters,
            ).collect { result ->
              when (result) {
                is Resource.Error -> send(Result.failure(result.error))
                is Resource.Loading<JellyseerrAccountDetails?> -> send(
                  Result.success(
                    JellyseerrAccountDetailsResult(
                      address = address,
                      accountDetails = result.data,
                    ),
                  ),
                )
                is Resource.Success<JellyseerrAccountDetails?> -> send(
                  Result.success(
                    JellyseerrAccountDetailsResult(
                      address = address,
                      accountDetails = result.data,
                    ),
                  ),
                )
              }
            }
          } else {
            send(
              Result.success(
                JellyseerrAccountDetailsResult(
                  address = "",
                  accountDetails = null,
                ),
              ),
            )
          }
        }
    }
}
