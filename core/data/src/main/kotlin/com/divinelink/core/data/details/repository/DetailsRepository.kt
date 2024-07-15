package com.divinelink.core.data.details.repository

import com.divinelink.core.model.account.AccountMediaDetails
import com.divinelink.core.model.credits.AggregateCredits
import com.divinelink.core.model.details.MediaDetails
import com.divinelink.core.model.details.Review
import com.divinelink.core.model.details.video.Video
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.network.media.model.details.DetailsRequestApi
import com.divinelink.core.network.media.model.details.reviews.ReviewsRequestApi
import com.divinelink.core.network.media.model.details.similar.SimilarRequestApi
import com.divinelink.core.network.media.model.details.videos.VideosRequestApi
import com.divinelink.core.network.media.model.details.watchlist.AddToWatchlistRequestApi
import com.divinelink.core.network.media.model.rating.AddRatingRequestApi
import com.divinelink.core.network.media.model.rating.DeleteRatingRequestApi
import com.divinelink.core.network.media.model.states.AccountMediaDetailsRequestApi
import kotlinx.coroutines.flow.Flow

/**
 * The data layer for any requests related to Movie Details Movies.
 */
interface DetailsRepository {

  fun fetchMovieDetails(request: DetailsRequestApi): Flow<Result<MediaDetails>>

  fun fetchMovieReviews(request: ReviewsRequestApi): Flow<Result<List<Review>>>

  fun fetchSimilarMovies(request: SimilarRequestApi): Flow<Result<List<MediaItem.Media>>>

  fun fetchVideos(request: VideosRequestApi): Flow<Result<List<Video>>>

  fun fetchAccountMediaDetails(
    request: AccountMediaDetailsRequestApi,
  ): Flow<Result<AccountMediaDetails>>

  fun submitRating(request: AddRatingRequestApi): Flow<Result<Unit>>

  fun deleteRating(request: DeleteRatingRequestApi): Flow<Result<Unit>>

  fun addToWatchlist(request: AddToWatchlistRequestApi): Flow<Result<Unit>>

  fun fetchLocalAggregateCredits(id: Long): Flow<Result<AggregateCredits>>

  fun fetchRemoteAggregateCredits(id: Long): Flow<Result<AggregateCredits>>

  fun fetchAggregateCredits(id: Long): Flow<Result<AggregateCredits>>
}
