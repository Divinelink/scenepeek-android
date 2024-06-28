package com.divinelink.core.domain.jellyseerr

import com.divinelink.core.commons.di.IoDispatcher
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.data.jellyseerr.JellyseerrRepository
import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.data.details.model.MediaDetailsParams
import com.divinelink.core.network.jellyseerr.model.JellyseerrRequestMediaBodyApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import javax.inject.Inject

open class RequestMediaUseCase @Inject constructor(
  private val repository: JellyseerrRepository,
  private val storage: PreferenceStorage,
  @IoDispatcher val dispatcher: CoroutineDispatcher,
) : FlowUseCase<MediaDetailsParams, Unit>(dispatcher) {

  override fun execute(parameters: MediaDetailsParams): Flow<Result<Unit>> = flow {
    val address = storage.jellyseerrAddress.first()

    if (address == null) {
      emit(Result.failure(IllegalArgumentException("Address cannot be null")))
      return@flow
    }

    val result = repository.requestMedia(
      JellyseerrRequestMediaBodyApi(
        address = address,
        mediaType = parameters.mediaType.value,
        mediaId = parameters.id,
        is4k = false,
      ),
    )

    result.last().fold(
      onSuccess = { emit(Result.success(Unit)) },
      onFailure = { emit(Result.failure(it)) },
    )
  }
}
