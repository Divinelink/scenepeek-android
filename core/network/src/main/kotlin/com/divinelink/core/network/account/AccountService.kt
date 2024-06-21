package com.divinelink.core.network.account

import com.divinelink.core.network.media.model.movie.MoviesResponseApi
import com.divinelink.core.network.media.model.tv.TvResponseApi
import kotlinx.coroutines.flow.Flow

interface AccountService {

  fun fetchMoviesWatchlist(
    page: Int,
    sortBy: String,
    accountId: String,
    sessionId: String,
  ): Flow<MoviesResponseApi>

  fun fetchTvShowsWatchlist(
    page: Int,
    sortBy: String,
    accountId: String,
    sessionId: String,
  ): Flow<TvResponseApi>
}
