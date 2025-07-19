package com.divinelink.core.data.account

import com.divinelink.core.model.PaginationData
import com.divinelink.core.model.list.ListItem
import com.divinelink.core.model.media.MediaItem
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

  suspend fun fetchUserLists(
    accountId: String,
    page: Int,
  ): Flow<Result<PaginationData<ListItem>>>
}
