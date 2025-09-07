package com.divinelink.core.domain.jellyseerr

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.data.jellyseerr.model.JellyseerrRequestParams
import com.divinelink.core.data.jellyseerr.repository.JellyseerrRepository
import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.model.exception.MissingJellyseerrHostAddressException
import com.divinelink.core.model.jellyseerr.request.MediaRequestResult
import com.divinelink.core.network.jellyseerr.model.JellyseerrRequestMediaBodyApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last

class RequestMediaUseCase(
  private val repository: JellyseerrRepository,
  private val storage: PreferenceStorage,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<JellyseerrRequestParams, MediaRequestResult>(dispatcher.default) {

  override fun execute(parameters: JellyseerrRequestParams): Flow<Result<MediaRequestResult>> =
    flow {
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
          serverId = parameters.serverId,
          profileId = parameters.profileId,
          rootFolder = parameters.rootFolder,
        ),
      )

      result.last().fold(
        onSuccess = { requestResult ->
          val requestDetails = repository
            .getRequestDetails(requestResult.requestId)
            .firstOrNull()?.getOrNull()

          if (requestDetails != null) {
            val updatedMediaInfo = requestResult.mediaInfo.copy(
              requests = requestResult.mediaInfo.requests.filter {
                it.id != requestDetails.id
              } + requestDetails,
              status = requestDetails.mediaStatus,
            )

            emit(Result.success(requestResult.copy(mediaInfo = updatedMediaInfo)))
          } else {
            emit(Result.success(requestResult))
          }
        },
        onFailure = { emit(Result.failure(it)) },
      )
    }
}
