package com.divinelink.core.network.account.service

import com.divinelink.core.model.sort.SortOption
import com.divinelink.core.network.media.model.details.watchlist.TMDBResponse
import com.divinelink.core.network.media.model.movie.MoviesResponseApi
import com.divinelink.core.network.media.model.tv.TvResponseApi
import kotlinx.coroutines.flow.Flow

interface AccountService {

  fun fetchMoviesWatchlist(
    page: Int,
    sortOption: SortOption,
    accountId: String,
    sessionId: String,
  ): Flow<MoviesResponseApi>

  fun fetchTvShowsWatchlist(
    page: Int,
    sortOption: SortOption,
    accountId: String,
    sessionId: String,
  ): Flow<TvResponseApi>

  fun fetchRatedMovies(
    page: Int,
    sortOption: SortOption,
    accountId: String,
    sessionId: String,
  ): Flow<MoviesResponseApi>

  fun fetchRatedTvShows(
    page: Int,
    sortOption: SortOption,
    accountId: String,
    sessionId: String,
  ): Flow<TvResponseApi>

  suspend fun submitEpisodeRating(
    showId: Int,
    season: Int,
    number: Int,
    rating: Int,
  ): Result<TMDBResponse>

  suspend fun clearEpisodeRating(
    showId: Int,
    season: Int,
    number: Int,
  ): Result<TMDBResponse>
}
