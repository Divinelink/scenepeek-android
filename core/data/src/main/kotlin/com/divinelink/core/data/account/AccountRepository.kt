package com.divinelink.core.data.account

import com.divinelink.core.data.media.repository.MediaListResult
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

  suspend fun fetchMoviesWatchlist(
    page: Int,
    accountId: Int,
  ): Flow<MediaListResult>
}
