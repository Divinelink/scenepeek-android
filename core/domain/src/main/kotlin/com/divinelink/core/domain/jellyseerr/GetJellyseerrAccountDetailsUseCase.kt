package com.divinelink.core.domain.jellyseerr

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.data.jellyseerr.repository.JellyseerrRepository
import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.model.jellyseerr.JellyseerrAccountDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class GetJellyseerrAccountDetailsUseCase(
  private val storage: PreferenceStorage,
  private val repository: JellyseerrRepository,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<Boolean, JellyseerrAccountDetails?>(dispatcher.io) {

  /**
   * @param parameters: If true, fetch from remote
   */
  override fun execute(parameters: Boolean): Flow<Result<JellyseerrAccountDetails?>> = channelFlow {
    val address = storage.jellyseerrAddress.firstOrNull()

    launch {
      repository
        .getLocalJellyseerrAccountDetails()
        .collect { details ->
          if (address == null) {
            send(Result.failure(Exception("No address found.")))
          } else {
            send(Result.success(details))
          }
        }
    }

    launch {
      if (parameters && address != null) {
        repository.getRemoteAccountDetails(address).collect { remoteResult ->
          remoteResult.onSuccess { remoteDetails ->
            repository.insertJellyseerrAccountDetails(remoteDetails)
            send(Result.success(remoteDetails))
          }.onFailure { error ->
            send(Result.failure(error))
          }
        }
      }
    }
  }
}
