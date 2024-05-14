package com.andreolas.movierama.details.domain.repository

import com.andreolas.movierama.base.data.remote.movies.mapper.map
import com.andreolas.movierama.details.ui.MovieDetailsException
import com.andreolas.movierama.details.ui.ReviewsException
import com.andreolas.movierama.details.ui.SimilarException
import com.andreolas.movierama.details.ui.VideosException
import com.divinelink.core.model.account.AccountMediaDetails
import com.divinelink.core.model.details.MediaDetails
import com.divinelink.core.model.details.Review
import com.divinelink.core.model.details.Video
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.movies.model.details.DetailsRequestApi
import com.divinelink.core.network.movies.model.details.reviews.ReviewsRequestApi
import com.divinelink.core.network.movies.model.details.reviews.toDomainReviewsList
import com.divinelink.core.network.movies.model.details.similar.SimilarRequestApi
import com.divinelink.core.network.movies.model.details.similar.toDomainMoviesList
import com.divinelink.core.network.movies.model.details.toDomainMedia
import com.divinelink.core.network.movies.model.details.videos.VideosRequestApi
import com.divinelink.core.network.movies.model.details.videos.toDomainVideosList
import com.divinelink.core.network.movies.model.rating.AddRatingRequestApi
import com.divinelink.core.network.movies.model.rating.DeleteRatingRequestApi
import com.divinelink.core.network.movies.model.states.AccountMediaDetailsRequestApi
import com.divinelink.core.network.movies.service.MovieService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProdDetailsRepository @Inject constructor(
  private val movieRemote: MovieService,
) : DetailsRepository {

  override fun fetchMovieDetails(request: DetailsRequestApi): Flow<Result<MediaDetails>> =
    movieRemote
      .fetchDetails(request)
      .map { apiResponse ->
        Result.success(apiResponse.toDomainMedia())
      }.catch {
        throw MovieDetailsException()
      }

  override fun fetchMovieReviews(request: ReviewsRequestApi): Flow<Result<List<Review>>> =
    movieRemote
      .fetchReviews(request)
      .map { apiResponse ->
        Result.success(apiResponse.toDomainReviewsList())
      }.catch {
        throw ReviewsException()
      }

  override fun fetchSimilarMovies(
    request: SimilarRequestApi,
  ): Flow<Result<List<MediaItem.Media>>> = movieRemote
    .fetchSimilarMovies(request)
    .map { apiResponse ->
      Result.success(apiResponse.toDomainMoviesList(MediaType.from(request.endpoint)))
    }.catch {
      throw SimilarException()
    }

  override fun fetchVideos(request: VideosRequestApi): Flow<Result<List<Video>>> = movieRemote
    .fetchVideos(request)
    .map { apiResponse ->
      Result.success(apiResponse.toDomainVideosList())
    }.catch {
      throw VideosException()
    }

  override fun fetchAccountMediaDetails(
    request: AccountMediaDetailsRequestApi
  ): Flow<Result<AccountMediaDetails>> = movieRemote
    .fetchAccountMediaDetails(request)
    .map { response ->
      Result.success(response.map())
    }

  override fun submitRating(request: AddRatingRequestApi): Flow<Result<Unit>> = movieRemote
    .submitRating(request)
    .map {
      Result.success(Unit)
    }

  override fun deleteRating(request: DeleteRatingRequestApi): Flow<Result<Unit>> = movieRemote
    .deleteRating(request)
    .map {
      Result.success(Unit)
    }
}
