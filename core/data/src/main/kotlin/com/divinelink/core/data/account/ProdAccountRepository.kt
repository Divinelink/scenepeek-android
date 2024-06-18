package com.divinelink.core.data.account

import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.network.PaginationData
import com.divinelink.core.network.account.AccountService
import com.divinelink.core.network.media.model.movie.toMoviesList
import com.divinelink.core.network.media.model.tv.toTvList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProdAccountRepository @Inject constructor(
  private val remote: AccountService,
) : AccountRepository {

  override suspend fun fetchMoviesWatchlist(
    page: Int,
    sortBy: String,
    accountId: String,
    sessionId: String,
  ): Flow<Result<PaginationData<MediaItem.Media>>> = remote
    .fetchMoviesWatchlist(page, sortBy, accountId, sessionId)
    .map { apiResponse ->
      Result.success(
        PaginationData<MediaItem.Media>(
          totalPages = apiResponse.totalPages,
          totalResults = apiResponse.totalResults,
          list = apiResponse.toMoviesList()
        )
      )
    }
    .catch { exception ->
      throw Exception(exception.message)
    }

  override suspend fun fetchTvShowsWatchlist(
    page: Int,
    sortBy: String,
    accountId: String,
    sessionId: String,
  ): Flow<Result<PaginationData<MediaItem.Media>>> = remote
    .fetchTvShowsWatchlist(page, sortBy, accountId, sessionId)
    .map { apiResponse ->
      Result.success(
        PaginationData<MediaItem.Media>(
          totalPages = apiResponse.totalPages,
          totalResults = apiResponse.totalResults,
          list = apiResponse.toTvList()
        )
      )
    }
    .catch { exception ->
      throw Exception(exception.message)
    }
}
