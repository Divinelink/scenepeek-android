package com.divinelink.core.network.account

import com.divinelink.core.network.client.TMDbClient
import com.divinelink.core.network.media.model.movie.MoviesResponseApi
import com.divinelink.core.network.media.model.tv.TvResponseApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProdAccountService(private val restClient: TMDbClient) : AccountService {

  override fun fetchMoviesWatchlist(
    page: Int,
    sortBy: String,
    accountId: String,
    sessionId: String,
  ): Flow<MoviesResponseApi> = flow {
    val url = "${restClient.tmdbUrl}/account/$accountId/watchlist/movies" +
      "?page=$page" +
      "&session_id=$sessionId" +
      "&sort_by=created_at.$sortBy"

    val response = restClient.get<MoviesResponseApi>(url = url)

    emit(response)
  }

  override fun fetchTvShowsWatchlist(
    page: Int,
    sortBy: String,
    accountId: String,
    sessionId: String,
  ): Flow<TvResponseApi> = flow {
    val url = "${restClient.tmdbUrl}/account/$accountId/watchlist/tv" +
      "?page=$page" +
      "&session_id=$sessionId" +
      "&sort_by=created_at.$sortBy"

    val response = restClient.get<TvResponseApi>(url = url)

    emit(response)
  }

  override fun fetchRatedMovies(
    page: Int,
    sortBy: String,
    accountId: String,
    sessionId: String,
  ): Flow<MoviesResponseApi> = flow {
    val url = "${restClient.tmdbUrl}/account/$accountId/rated/movies" +
      "?page=$page" +
      "&session_id=$sessionId" +
      "&sort_by=created_at.$sortBy"

    val response = restClient.get<MoviesResponseApi>(url = url)

    emit(response)
  }

  override fun fetchRatedTvShows(
    page: Int,
    sortBy: String,
    accountId: String,
    sessionId: String,
  ): Flow<TvResponseApi> = flow {
    val url = "${restClient.tmdbUrl}/account/$accountId/rated/tv" +
      "?page=$page" +
      "&session_id=$sessionId" +
      "&sort_by=created_at.$sortBy"

    val response = restClient.get<TvResponseApi>(url = url)

    emit(response)
  }
}
