package com.divinelink.feature.details.usecase

import com.divinelink.core.commons.di.IoDispatcher
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.details.repository.DetailsRepository
import com.divinelink.core.data.session.model.SessionException
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.media.model.rating.AddRatingRequestApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import javax.inject.Inject

data class SubmitRatingParameters(
  val id: Int,
  val mediaType: MediaType,
  val rating: Int
)

open class SubmitRatingUseCase @Inject constructor(
  private val sessionStorage: SessionStorage,
  private val repository: DetailsRepository,
  @IoDispatcher val dispatcher: CoroutineDispatcher,
) : FlowUseCase<SubmitRatingParameters, Unit>(dispatcher) {
  override fun execute(parameters: SubmitRatingParameters): Flow<Result<Unit>> = flow {
    val sessionId = sessionStorage.sessionId

    if (sessionId == null) {
      emit(Result.failure(SessionException.NoSession()))
      return@flow
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
          rating = parameters.rating
        )

        else -> throw IllegalArgumentException("Unsupported media type: ${parameters.mediaType}")
      }

      val response = repository.submitRating(request).last()

      emit(Result.success(response.data))
    }
  }
}
