package com.divinelink.core.network.account

import com.divinelink.core.network.media.model.movie.MoviesResponseApi
import kotlinx.coroutines.flow.Flow

interface AccountService {

  fun fetchMoviesWatchlist(
    page: Int,
    sortBy: String,
    accountId: String
  ): Flow<MoviesResponseApi>
}
