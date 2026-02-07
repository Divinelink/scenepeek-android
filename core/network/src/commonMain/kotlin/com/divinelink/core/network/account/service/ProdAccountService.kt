package com.divinelink.core.network.account.service

import com.divinelink.core.datastore.auth.SavedStateStorage
import com.divinelink.core.datastore.auth.tmdbSessionId
import com.divinelink.core.model.exception.SessionException
import com.divinelink.core.network.account.util.buildSubmitEpisodeRating
import com.divinelink.core.network.client.TMDbClient
import com.divinelink.core.network.media.model.details.watchlist.TMDBResponse
import com.divinelink.core.network.media.model.movie.MoviesResponseApi
import com.divinelink.core.network.media.model.tv.TvResponseApi
import com.divinelink.core.network.model.ValueRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProdAccountService(
  private val restClient: TMDbClient,
  private val storage: SavedStateStorage,
) : AccountService {

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

  override suspend fun submitEpisodeRating(
    showId: Int,
    season: Int,
    number: Int,
    rating: Int,
  ): Result<TMDBResponse> = runCatching {
    if (storage.tmdbSessionId == null) {
      throw SessionException.Unauthenticated()
    } else {
      val url = buildSubmitEpisodeRating(
        showId = showId,
        season = season,
        number = number,
        sessionId = storage.tmdbSessionId!!,
      )

      restClient.post<ValueRequest, TMDBResponse>(
        url = url,
        body = ValueRequest(rating.toFloat()),
      )
    }
  }
}
