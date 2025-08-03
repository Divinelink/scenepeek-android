package com.divinelink.feature.details.media.usecase

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.details.repository.DetailsRepository
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.model.exception.SessionException
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.media.model.rating.AddRatingRequestApi

data class SubmitRatingParameters(
  val id: Int,
  val mediaType: MediaType,
  val rating: Int,
)

open class SubmitRatingUseCase(
  private val sessionStorage: SessionStorage,
  private val repository: DetailsRepository,
  val dispatcher: DispatcherProvider,
) : UseCase<SubmitRatingParameters, Unit>(dispatcher.default) {
  override suspend fun execute(parameters: SubmitRatingParameters) {
    val sessionId = sessionStorage.sessionId

    if (sessionId == null) {
      Result.failure<Exception>(SessionException.Unauthenticated())
    } else {
      val request = when (parameters.mediaType) {
        MediaType.MOVIE -> AddRatingRequestApi.Movie(
          movieId = parameters.id,
          sessionId = sessionId,
          rating = parameters.rating,
        )
        MediaType.TV -> AddRatingRequestApi.TV(
          seriesId = parameters.id,
          sessionId = sessionId,
          rating = parameters.rating,
        )

        else -> throw IllegalArgumentException("Unsupported media type: ${parameters.mediaType}")
      }

      val response = repository.submitRating(request)

      Result.success(response.data)
    }
  }
}
