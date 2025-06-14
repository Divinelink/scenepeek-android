package com.divinelink.core.domain.jellyseerr

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.data.jellyseerr.model.JellyseerrRequestParams
import com.divinelink.core.data.jellyseerr.repository.JellyseerrRepository
import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.model.exception.MissingJellyseerrHostAddressException
import com.divinelink.core.model.jellyseerr.request.JellyseerrMediaRequestResponse
import com.divinelink.core.network.jellyseerr.model.JellyseerrRequestMediaBodyApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last

class RequestMediaUseCase(
  private val repository: JellyseerrRepository,
  private val storage: PreferenceStorage,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<JellyseerrRequestParams, JellyseerrMediaRequestResponse>(dispatcher.default) {

  override fun execute(
    parameters: JellyseerrRequestParams,
  ): Flow<Result<JellyseerrMediaRequestResponse>> = flow {
    val address = storage.jellyseerrAddress.first()

    if (address == null) {
      emit(Result.failure(MissingJellyseerrHostAddressException()))
      return@flow
    }

    val result = repository.requestMedia(
      JellyseerrRequestMediaBodyApi(
        mediaType = parameters.mediaType,
        mediaId = parameters.mediaId,
        is4k = parameters.is4k,
        seasons = parameters.seasons,
      ),
    )

    result.last().fold(
      onSuccess = { emit(Result.success(it)) },
      onFailure = { emit(Result.failure(it)) },
    )
  }
}
