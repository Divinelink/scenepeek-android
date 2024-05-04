package com.andreolas.movierama.details.domain.usecase

import com.andreolas.movierama.base.data.remote.movies.dto.details.DetailsRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.reviews.ReviewsRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.similar.SimilarRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.videos.VideosRequestApi
import com.andreolas.movierama.base.di.IoDispatcher
import com.andreolas.movierama.details.domain.model.MovieDetailsException
import com.andreolas.movierama.details.domain.model.MovieDetailsResult
import com.andreolas.movierama.details.domain.model.ReviewsException
import com.andreolas.movierama.details.domain.model.SimilarException
import com.andreolas.movierama.details.domain.model.VideosException
import com.andreolas.movierama.details.domain.repository.DetailsRepository
import com.andreolas.movierama.home.domain.model.MediaType
import com.andreolas.movierama.home.domain.repository.MoviesRepository
import gr.divinelink.core.util.domain.FlowUseCase
import gr.divinelink.core.util.domain.data
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
  private val moviesRepository: MoviesRepository,
  @IoDispatcher val dispatcher: CoroutineDispatcher,
) : FlowUseCase<DetailsRequestApi, MovieDetailsResult>(dispatcher) {
  override fun execute(parameters: DetailsRequestApi): Flow<Result<MovieDetailsResult>> {
    val requestApi = when (parameters) {
      is DetailsRequestApi.Movie -> parameters
      is DetailsRequestApi.TV -> parameters
    }

    val favorite = flow {
      coroutineScope {
        val result = moviesRepository.checkIfMediaIsFavorite(
          id = requestApi.id,
          mediaType = MediaType.from(requestApi.endpoint)
        )
        emit(result)
      }
    }.flowOn(dispatcher)

    val detailsRequestApi = when (parameters) {
      is DetailsRequestApi.Movie -> DetailsRequestApi.Movie(parameters.id)
      is DetailsRequestApi.TV -> DetailsRequestApi.TV(parameters.id)
    }

    val details = repository.fetchMovieDetails(
      request = detailsRequestApi,
    ).catch {
      emit(Result.failure(MovieDetailsException()))
    }

    val reviewsApi = when (parameters) {
      is DetailsRequestApi.Movie -> ReviewsRequestApi.Movie(parameters.id)
      is DetailsRequestApi.TV -> ReviewsRequestApi.TV(parameters.id)
    }

    val reviews = repository.fetchMovieReviews(reviewsApi)
      .catch {
        emit(Result.failure(ReviewsException()))
      }

    val similarApi = when (parameters) {
      is DetailsRequestApi.Movie -> SimilarRequestApi.Movie(parameters.id)
      is DetailsRequestApi.TV -> SimilarRequestApi.TV(parameters.id)
    }
    val similar = repository.fetchSimilarMovies(
      request = similarApi,
    ).catch {
      emit(Result.failure(SimilarException()))
    }

    val videos = repository.fetchVideos(
      request = VideosRequestApi(
        movieId = parameters.id,
      )
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
              detailsFlow.data.copy(isFavorite = favoriteFlow.data)
            )
          )
        )
      }.onFailure {
        emit(Result.failure(MovieDetailsException()))
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
              trailer = videosFlow.data.firstOrNull { it.officialTrailer }
            )
          )
        )
      }
    }
  }
}
