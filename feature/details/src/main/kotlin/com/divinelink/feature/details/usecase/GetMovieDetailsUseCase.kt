package com.divinelink.feature.details.usecase

import com.divinelink.core.commons.di.IoDispatcher
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.details.model.InvalidMediaTypeException
import com.divinelink.core.data.details.model.MediaDetailsException
import com.divinelink.core.data.details.model.ReviewsException
import com.divinelink.core.data.details.model.SimilarException
import com.divinelink.core.data.details.model.VideosException
import com.divinelink.core.data.details.repository.DetailsRepository
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.media.model.details.DetailsRequestApi
import com.divinelink.core.network.media.model.details.reviews.ReviewsRequestApi
import com.divinelink.core.network.media.model.details.similar.SimilarRequestApi
import com.divinelink.core.network.media.model.details.videos.VideosRequestApi
import com.divinelink.feature.details.ui.MovieDetailsResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@Suppress("LongMethod")
open class GetMovieDetailsUseCase @Inject constructor(
  private val repository: DetailsRepository,
  private val mediaRepository: MediaRepository,
  @IoDispatcher val dispatcher: CoroutineDispatcher,
) : FlowUseCase<DetailsRequestApi, MovieDetailsResult>(dispatcher) {
  override fun execute(parameters: DetailsRequestApi): Flow<Result<MovieDetailsResult>> {
    if (parameters == DetailsRequestApi.Unknown) {
      return flow {
        emit(Result.failure(MediaDetailsException()))
      }
    }

    val requestApi = when (parameters) {
      is DetailsRequestApi.Movie -> parameters
      is DetailsRequestApi.TV -> parameters
      DetailsRequestApi.Unknown -> return flow {
        emit(Result.failure(InvalidMediaTypeException()))
      }
    }

    val favorite = flow {
      coroutineScope {
        val result = mediaRepository.checkIfMediaIsFavorite(
          id = requestApi.id,
          mediaType = MediaType.from(requestApi.endpoint),
        )
        emit(result)
      }
    }.flowOn(dispatcher)

    val detailsRequestApi = when (parameters) {
      is DetailsRequestApi.Movie -> DetailsRequestApi.Movie(parameters.id)
      is DetailsRequestApi.TV -> DetailsRequestApi.TV(parameters.id)
      DetailsRequestApi.Unknown -> {
        return flow {
          emit(Result.failure(InvalidMediaTypeException()))
        }
      }
    }

    val details = repository.fetchMovieDetails(
      request = detailsRequestApi,
    ).catch {
      emit(Result.failure(MediaDetailsException()))
    }

    val reviewsApi = when (parameters) {
      is DetailsRequestApi.Movie -> ReviewsRequestApi.Movie(parameters.id)
      is DetailsRequestApi.TV -> ReviewsRequestApi.TV(parameters.id)
      DetailsRequestApi.Unknown -> {
        return flow {
          emit(Result.failure(InvalidMediaTypeException()))
        }
      }
    }

    val reviews = repository.fetchMovieReviews(reviewsApi)
      .catch {
        emit(Result.failure(ReviewsException()))
      }

    val similarApi = when (parameters) {
      is DetailsRequestApi.Movie -> SimilarRequestApi.Movie(parameters.id)
      is DetailsRequestApi.TV -> SimilarRequestApi.TV(parameters.id)
      DetailsRequestApi.Unknown -> {
        return flow {
          emit(Result.failure(InvalidMediaTypeException()))
        }
      }
    }

    val similar = repository.fetchSimilarMovies(
      request = similarApi,
    ).catch {
      emit(Result.failure(SimilarException()))
    }

    val videos = repository.fetchVideos(
      request = VideosRequestApi(
        movieId = parameters.id,
      ),
    ).catch {
      emit(Result.failure(VideosException()))
    }

    return combineTransform(
      flow = details,
      flow2 = reviews,
      flow3 = similar,
      flow4 = favorite,
      flow5 = videos,
    ) { detailsFlow, reviewsFlow, similarFlow, favoriteFlow, videosFlow ->
      detailsFlow.onSuccess {
        emit(
          Result.success(
            MovieDetailsResult.DetailsSuccess(
              detailsFlow.data.copy(isFavorite = favoriteFlow.data),
            ),
          ),
        )
      }.onFailure {
        emit(Result.failure(MediaDetailsException()))
      }

      if (reviewsFlow.isSuccess) {
        emit(Result.success(MovieDetailsResult.ReviewsSuccess(reviews = reviewsFlow.data)))
      }

      if (similarFlow.isSuccess) {
        emit(Result.success(MovieDetailsResult.SimilarSuccess(similar = similarFlow.data)))
      }

      if (videosFlow.isSuccess) {
        emit(
          Result.success(
            MovieDetailsResult.VideosSuccess(
              trailer = videosFlow.data.firstOrNull { it.officialTrailer },
            ),
          ),
        )
      }
    }
  }
}
