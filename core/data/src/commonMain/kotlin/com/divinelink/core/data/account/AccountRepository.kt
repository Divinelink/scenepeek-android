package com.divinelink.core.data.account

import com.divinelink.core.model.PaginationData
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.network.media.model.rating.DeleteRatingRequestApi
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

  suspend fun fetchMoviesWatchlist(
    page: Int,
    sortBy: String,
    accountId: String,
    sessionId: String,
  ): Flow<Result<PaginationData<MediaItem.Media>>>

  suspend fun fetchTvShowsWatchlist(
    page: Int,
    sortBy: String,
    accountId: String,
    sessionId: String,
  ): Flow<Result<PaginationData<MediaItem.Media>>>

  suspend fun fetchRatedMovies(
    page: Int,
    sortBy: String,
    accountId: String,
    sessionId: String,
  ): Flow<Result<PaginationData<MediaItem.Media>>>

  suspend fun fetchRatedTvShows(
    page: Int,
    sortBy: String,
    accountId: String,
    sessionId: String,
  ): Flow<Result<PaginationData<MediaItem.Media>>>

  suspend fun submitEpisodeRating(
    showId: Int,
    season: Int,
    number: Int,
    rating: Int,
  ): Result<Unit>

  suspend fun deleteEpisodeRating(request: DeleteRatingRequestApi): Result<Unit>
}
