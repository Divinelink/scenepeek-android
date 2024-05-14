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
import com.divinelink.core.network.media.model.details.DetailsRequestApi
import com.divinelink.core.network.media.model.details.reviews.ReviewsRequestApi
import com.divinelink.core.network.media.model.details.reviews.toDomainReviewsList
import com.divinelink.core.network.media.model.details.similar.SimilarRequestApi
import com.divinelink.core.network.media.model.details.similar.toDomainMoviesList
import com.divinelink.core.network.media.model.details.toDomainMedia
import com.divinelink.core.network.media.model.details.videos.VideosRequestApi
import com.divinelink.core.network.media.model.details.videos.toDomainVideosList
import com.divinelink.core.network.media.model.rating.AddRatingRequestApi
import com.divinelink.core.network.media.model.rating.DeleteRatingRequestApi
import com.divinelink.core.network.media.model.states.AccountMediaDetailsRequestApi
import com.divinelink.core.network.media.service.MediaService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProdDetailsRepository @Inject constructor(
  private val mediaRemote: MediaService,
) : DetailsRepository {

  override fun fetchMovieDetails(request: DetailsRequestApi): Flow<Result<MediaDetails>> =
    mediaRemote
      .fetchDetails(request)
      .map { apiResponse ->
        Result.success(apiResponse.toDomainMedia())
      }.catch {
        throw MovieDetailsException()
      }

  override fun fetchMovieReviews(request: ReviewsRequestApi): Flow<Result<List<Review>>> =
    mediaRemote
      .fetchReviews(request)
      .map { apiResponse ->
        Result.success(apiResponse.toDomainReviewsList())
      }.catch {
        throw ReviewsException()
      }

  override fun fetchSimilarMovies(
    request: SimilarRequestApi,
  ): Flow<Result<List<MediaItem.Media>>> = mediaRemote
    .fetchSimilarMovies(request)
    .map { apiResponse ->
      Result.success(apiResponse.toDomainMoviesList(MediaType.from(request.endpoint)))
    }.catch {
      throw SimilarException()
    }

  override fun fetchVideos(request: VideosRequestApi): Flow<Result<List<Video>>> = mediaRemote
    .fetchVideos(request)
    .map { apiResponse ->
      Result.success(apiResponse.toDomainVideosList())
    }.catch {
      throw VideosException()
    }

  override fun fetchAccountMediaDetails(
    request: AccountMediaDetailsRequestApi
  ): Flow<Result<AccountMediaDetails>> = mediaRemote
    .fetchAccountMediaDetails(request)
    .map { response ->
      Result.success(response.map())
    }

  override fun submitRating(request: AddRatingRequestApi): Flow<Result<Unit>> = mediaRemote
    .submitRating(request)
    .map {
      Result.success(Unit)
    }

  override fun deleteRating(request: DeleteRatingRequestApi): Flow<Result<Unit>> = mediaRemote
    .deleteRating(request)
    .map {
      Result.success(Unit)
    }
}
