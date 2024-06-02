package com.andreolas.movierama.details.domain.usecase

import com.andreolas.movierama.base.di.IoDispatcher
import com.andreolas.movierama.session.SessionStorage
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.details.repository.DetailsRepository
import com.divinelink.core.data.session.model.SessionException
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.media.model.rating.DeleteRatingRequestApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import javax.inject.Inject

data class DeleteRatingParameters(
  val id: Int,
  val mediaType: MediaType,
)

open class DeleteRatingUseCase @Inject constructor(
  private val sessionStorage: SessionStorage,
  private val repository: DetailsRepository,
  @IoDispatcher val dispatcher: CoroutineDispatcher,
) : FlowUseCase<DeleteRatingParameters, Unit>(dispatcher) {
  override fun execute(parameters: DeleteRatingParameters): Flow<Result<Unit>> = flow {
    val sessionId = sessionStorage.sessionId

    if (sessionId == null) {
      emit(Result.failure(SessionException.NoSession()))
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
