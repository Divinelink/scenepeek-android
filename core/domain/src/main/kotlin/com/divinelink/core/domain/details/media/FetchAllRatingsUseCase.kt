package com.divinelink.core.domain.details.media

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.model.details.MediaDetails
import com.divinelink.core.model.details.rating.RatingCount
import com.divinelink.core.model.details.rating.RatingDetails
import com.divinelink.core.model.details.rating.RatingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow

class FetchAllRatingsUseCase(
  private val repository: MediaRepository,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<MediaDetails, RatingCount>(dispatcher.io) {

  override fun execute(parameters: MediaDetails): Flow<Result<RatingCount>> = channelFlow {
    if (parameters.ratingCount.ratings[RatingSource.IMDB] == RatingDetails.Initial) {
      parameters.imdbId?.let { imdbId ->
        repository.fetchIMDbDetails(imdbId).collect { result ->
          result.fold(
            onSuccess = { imdbDetails ->
              val updatedRatingCount = parameters.ratingCount.updateRating(
                source = RatingSource.IMDB,
                rating = imdbDetails ?: RatingDetails.Unavailable,
              )
              send(Result.success(updatedRatingCount))
            },
            onFailure = { error ->
              send(Result.failure(error))
            },
          )
        }
      }
    }
  }
}
