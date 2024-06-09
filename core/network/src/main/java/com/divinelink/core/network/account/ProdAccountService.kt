package com.divinelink.core.network.account

import com.divinelink.core.network.client.RestClient
import com.divinelink.core.network.media.model.movie.MoviesResponseApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProdAccountService @Inject constructor(
  private val restClient: RestClient,
) : AccountService {

  override fun fetchMoviesWatchlist(page: Int, accountId: Int): Flow<MoviesResponseApi> = flow {
    val url = "${restClient.tmdbUrl}/account/$accountId/watchlist/movies?page=$page"

    val response = restClient.get<MoviesResponseApi>(url = url)

    emit(response)
  }
}
