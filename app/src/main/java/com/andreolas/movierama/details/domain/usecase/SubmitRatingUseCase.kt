package com.andreolas.movierama.details.domain.usecase

import com.andreolas.movierama.base.di.IoDispatcher
import com.andreolas.movierama.details.domain.exception.SessionException
import com.andreolas.movierama.details.domain.repository.DetailsRepository
import com.andreolas.movierama.session.SessionStorage
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.movies.model.rating.AddRatingRequestApi
import gr.divinelink.core.util.domain.FlowUseCase
import gr.divinelink.core.util.domain.data
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
