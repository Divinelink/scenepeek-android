package com.divinelink.core.domain.jellyseerr

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.data.jellyseerr.repository.JellyseerrRepository
import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaInfo
import com.divinelink.core.network.media.model.MediaRequestApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

data class DeleteRequestParameters(
  val requestId: Int,
  val mediaRequest: MediaRequestApi,
)

class DeleteRequestUseCase(
  private val repository: JellyseerrRepository,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<DeleteRequestParameters, JellyseerrMediaInfo>(dispatcher.default) {

  override fun execute(parameters: DeleteRequestParameters): Flow<Result<JellyseerrMediaInfo>> =
    flow {
      repository
        .deleteRequest(parameters.requestId)
        .onSuccess {
          when (val mediaRequest = parameters.mediaRequest) {
            is MediaRequestApi.Movie -> {
              val details = repository.getMovieDetails(mediaRequest.movieId).firstOrNull()

              if (details != null) {
                emit(Result.success(details))
              } else {
                emit(Result.failure(Exception("Movie details not found")))
              }
            }
            is MediaRequestApi.TV -> {
              val details = repository.getTvDetails(mediaRequest.seriesId).firstOrNull()

              if (details != null) {
                emit(Result.success(details))
              } else {
                emit(Result.failure(Exception("TV details not found")))
              }
            }
            else -> emit(Result.failure(Exception("Unsupported media request type")))
          }
        }
    }
}
