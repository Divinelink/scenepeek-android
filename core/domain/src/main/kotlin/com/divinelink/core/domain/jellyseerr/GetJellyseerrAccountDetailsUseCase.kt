package com.divinelink.core.domain.jellyseerr

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.data.jellyseerr.repository.JellyseerrRepository
import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.model.jellyseerr.JellyseerrAccountDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class GetJellyseerrAccountDetailsUseCase(
  private val storage: PreferenceStorage,
  private val repository: JellyseerrRepository,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<Boolean, JellyseerrAccountDetails?>(dispatcher.default) {

  /**
   * @param parameters: If true, fetch from remote
   */
  override fun execute(parameters: Boolean): Flow<Result<JellyseerrAccountDetails?>> = channelFlow {
    val address = storage.jellyseerrAddress.first()

    launch {
      repository.getLocalJellyseerrAccountDetails().collect { localDetails ->
        if (storage.jellyseerrAddress.first() == null) {
          send(Result.success(null))
        } else {
          send(Result.success(localDetails))
        }
      }
    }

    launch {
      if (parameters && address != null) {
        repository.getRemoteAccountDetails(address).first().fold(
          onSuccess = {
            repository.insertJellyseerrAccountDetails(it)
            send(Result.success(it))
          },
          onFailure = {
            send(Result.failure(it))
          },
        )
      }
    }
  }
}
