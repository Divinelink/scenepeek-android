package com.divinelink.core.data.account

import com.divinelink.core.model.PaginationData
import com.divinelink.core.model.list.ListItem
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.network.account.mapper.map
import com.divinelink.core.network.account.service.AccountService
import com.divinelink.core.network.media.model.movie.map
import com.divinelink.core.network.media.model.tv.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProdAccountRepository(private val remote: AccountService) : AccountRepository {

  override suspend fun fetchMoviesWatchlist(
    page: Int,
    sortBy: String,
    accountId: String,
    sessionId: String,
  ): Flow<Result<PaginationData<MediaItem.Media>>> = remote
    .fetchMoviesWatchlist(page, sortBy, accountId, sessionId)
    .map { apiResponse ->
      Result.success(apiResponse.map())
    }

  override suspend fun fetchTvShowsWatchlist(
    page: Int,
    sortBy: String,
    accountId: String,
    sessionId: String,
  ): Flow<Result<PaginationData<MediaItem.Media>>> = remote
    .fetchTvShowsWatchlist(page, sortBy, accountId, sessionId)
    .map { apiResponse ->
      Result.success(apiResponse.map())
    }

  override suspend fun fetchRatedMovies(
    page: Int,
    sortBy: String,
    accountId: String,
    sessionId: String,
  ): Flow<Result<PaginationData<MediaItem.Media>>> = remote
    .fetchRatedMovies(page, sortBy, accountId, sessionId)
    .map { apiResponse ->
      Result.success(apiResponse.map())
    }

  override suspend fun fetchRatedTvShows(
    page: Int,
    sortBy: String,
    accountId: String,
    sessionId: String,
  ): Flow<Result<PaginationData<MediaItem.Media>>> = remote
    .fetchRatedTvShows(page, sortBy, accountId, sessionId)
    .map { apiResponse ->
      Result.success(apiResponse.map())
    }

  override suspend fun fetchUserLists(accountId: String): Flow<Result<PaginationData<ListItem>>> =
    remote
      .fetchUserLists(accountId)
      .map { Result.success(it.map()) }
}
