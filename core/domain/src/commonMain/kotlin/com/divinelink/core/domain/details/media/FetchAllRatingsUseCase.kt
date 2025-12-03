package com.divinelink.core.domain.details.media

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.data.details.repository.DetailsRepository
import com.divinelink.core.model.details.MediaDetails
import com.divinelink.core.model.details.Movie
import com.divinelink.core.model.details.rating.RatingDetails
import com.divinelink.core.model.details.rating.RatingSource
import com.divinelink.core.model.media.MediaType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch

class FetchAllRatingsUseCase(
  private val repository: DetailsRepository,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<MediaDetails, Pair<RatingSource, RatingDetails>>(dispatcher.io) {

  override fun execute(parameters: MediaDetails): Flow<Result<Pair<RatingSource, RatingDetails>>> =
    channelFlow {
      val imdbId = parameters.imdbId

      if (imdbId == null) {
        send(Result.success(RatingSource.IMDB to RatingDetails.Unavailable))
        send(Result.success(RatingSource.TRAKT to RatingDetails.Unavailable))
        return@channelFlow
      }

      launch {
        if (parameters.ratingCount.ratings[RatingSource.IMDB] == RatingDetails.Initial) {
          repository.fetchIMDbDetails(imdbId).collect { result ->
            result.fold(
              onSuccess = { imdbDetails ->
                send(
                  Result.success(RatingSource.IMDB to (imdbDetails ?: RatingDetails.Unavailable)),
                )
              },
              onFailure = { error ->
                send(Result.failure(error))
              },
            )
          }
        }
      }

      launch {
        if (parameters.ratingCount.ratings[RatingSource.TRAKT] == RatingDetails.Initial) {
          val mediaType = if (parameters is Movie) MediaType.MOVIE else MediaType.TV
          repository.fetchTraktRating(mediaType, imdbId).collect { result ->
            result.fold(
              onSuccess = { traktDetails ->
                send(Result.success(RatingSource.TRAKT to traktDetails))
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
