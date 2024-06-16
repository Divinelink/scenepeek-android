package com.divinelink.core.data.account

import com.divinelink.core.data.media.repository.MediaListResult
import com.divinelink.core.network.account.AccountService
import com.divinelink.core.network.media.model.movie.toMoviesList
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
  ): Flow<MediaListResult> = remote
    .fetchMoviesWatchlist(page, sortBy, accountId)
    .map { apiResponse ->
      Result.success(apiResponse.toMoviesList())
    }
    .catch { exception ->
      throw Exception(exception.message)
    }
}
