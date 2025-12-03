package com.divinelink.core.domain.details.media

import com.divinelink.core.commons.data
import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.data.details.repository.DetailsRepository
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.model.exception.SessionException
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.media.model.rating.DeleteRatingRequestApi

data class DeleteRatingParameters(
  val id: Int,
  val mediaType: MediaType,
)

open class DeleteRatingUseCase(
  private val sessionStorage: SessionStorage,
  private val repository: DetailsRepository,
  val dispatcher: DispatcherProvider,
) : UseCase<DeleteRatingParameters, Unit>(dispatcher.default) {
  override suspend fun execute(parameters: DeleteRatingParameters) {
    val sessionId = sessionStorage.sessionId

    if (sessionId == null) {
      throw SessionException.Unauthenticated()
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

      val response = repository.deleteRating(request)

      Result.success(response.data)
    }
  }
}
