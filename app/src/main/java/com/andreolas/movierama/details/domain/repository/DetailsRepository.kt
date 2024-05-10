package com.andreolas.movierama.details.domain.repository

import com.andreolas.movierama.base.data.remote.movies.dto.account.states.AccountMediaDetailsRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.DetailsRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.reviews.ReviewsRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.similar.SimilarRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.videos.VideosRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.rating.AddRatingRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.rating.DeleteRatingRequestApi
import com.andreolas.movierama.details.domain.model.MediaDetails
import com.andreolas.movierama.details.domain.model.Review
import com.andreolas.movierama.details.domain.model.Video
import com.andreolas.movierama.details.domain.model.account.AccountMediaDetails
import com.andreolas.movierama.home.domain.model.MediaItem
import kotlinx.coroutines.flow.Flow

/**
 * The data layer for any requests related to Movie Details Movies.
 */
interface DetailsRepository {

  fun fetchMovieDetails(
    request: DetailsRequestApi,
  ): Flow<Result<MediaDetails>>

  fun fetchMovieReviews(
    request: ReviewsRequestApi,
  ): Flow<Result<List<Review>>>

  fun fetchSimilarMovies(
    request: SimilarRequestApi,
  ): Flow<Result<List<MediaItem.Media>>>

  fun fetchVideos(
    request: VideosRequestApi,
  ): Flow<Result<List<Video>>>

  fun fetchAccountMediaDetails(
    request: AccountMediaDetailsRequestApi
  ): Flow<Result<AccountMediaDetails>>

  fun submitRating(
    request: AddRatingRequestApi
  ): Flow<Result<Unit>>

  fun deleteRating(
    request: DeleteRatingRequestApi
  ): Flow<Result<Unit>>
}
