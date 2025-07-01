package com.divinelink.feature.details.media.usecase

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.details.repository.DetailsRepository
import com.divinelink.core.model.exception.SessionException
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.media.model.rating.DeleteRatingRequestApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last

data class DeleteRatingParameters(
  val id: Int,
  val mediaType: MediaType,
)

open class DeleteRatingUseCase(
  private val sessionStorage: SessionStorage,
  private val repository: DetailsRepository,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<DeleteRatingParameters, Unit>(dispatcher.io) {
  override fun execute(parameters: DeleteRatingParameters): Flow<Result<Unit>> = flow {
    val sessionId = sessionStorage.sessionId

    if (sessionId == null) {
      emit(Result.failure(SessionException.Unauthenticated()))
      return@flow
    } else {
      val request = when (parameters.mediaType) {
        MediaType.MOVIE -> DeleteRatingRequestApi.Movie(
          movieId = parameters.id,
          sessionId = sessionId,
        )
        MediaType.TV -> DeleteRatingRequestApi.TV(
          seriesId = parameters.id,
          sessionId = sessionId,
        )

        else -> throw IllegalArgumentException("Unsupported media type: ${parameters.mediaType}")
      }

      val response = repository.deleteRating(request).last()

      emit(Result.success(response.data))
    }
  }
}
