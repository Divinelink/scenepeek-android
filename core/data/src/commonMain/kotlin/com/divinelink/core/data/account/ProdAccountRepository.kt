package com.divinelink.core.data.account

import com.divinelink.core.database.media.dao.MediaDao
import com.divinelink.core.model.PaginationData
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.sort.SortOption
import com.divinelink.core.network.account.service.AccountService
import com.divinelink.core.network.media.model.movie.map
import com.divinelink.core.network.media.model.tv.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class ProdAccountRepository(
  private val remote: AccountService,
  private val dao: MediaDao,
) : AccountRepository {

  override suspend fun fetchMoviesWatchlist(
    page: Int,
    sortOption: SortOption,
    accountId: String,
    sessionId: String,
  ): Flow<Result<PaginationData<MediaItem.Media>>> = combine(
    remote.fetchMoviesWatchlist(
      page = page,
      sortOption = sortOption,
      accountId = accountId,
      sessionId = sessionId,
    ),
    dao.getFavoriteMediaIds(MediaType.MOVIE),
  ) { response, favoriteIds ->
    val data = response.map()
    val favoriteSet = favoriteIds.toSet()

    val updatedMovies = data.list.map { media ->
      (media as MediaItem.Media.Movie).copy(
        isFavorite = media.id in favoriteSet,
      )
    }

    Result.success(data.copy(list = updatedMovies))
  }

  override suspend fun fetchTvShowsWatchlist(
    page: Int,
    sortOption: SortOption,
    accountId: String,
    sessionId: String,
  ): Flow<Result<PaginationData<MediaItem.Media>>> = combine(
    remote.fetchTvShowsWatchlist(
      page = page,
      sortOption = sortOption,
      accountId = accountId,
      sessionId = sessionId,
    ),
    dao.getFavoriteMediaIds(MediaType.TV),
  ) { response, favoriteIds ->
    val data = response.map()
    val favoriteSet = favoriteIds.toSet()

    val updatedTvShows = data.list.map { media ->
      (media as MediaItem.Media.TV).copy(
        isFavorite = media.id in favoriteSet,
      )
    }

    Result.success(data.copy(list = updatedTvShows))
  }

  override suspend fun fetchRatedMovies(
    page: Int,
    sortOption: SortOption,
    accountId: String,
    sessionId: String,
  ): Flow<Result<PaginationData<MediaItem.Media>>> = combine(
    remote.fetchRatedMovies(
      page = page,
      sortOption = sortOption,
      accountId = accountId,
      sessionId = sessionId,
    ),
    dao.getFavoriteMediaIds(MediaType.MOVIE),
  ) { response, favoriteIds ->
    val data = response.map()
    val favoriteSet = favoriteIds.toSet()

    val updatedMovies = data.list.map { media ->
      (media as MediaItem.Media.Movie).copy(
        isFavorite = media.id in favoriteSet,
      )
    }

    Result.success(data.copy(list = updatedMovies))
  }

  override suspend fun fetchRatedTvShows(
    page: Int,
    sortOption: SortOption,
    accountId: String,
    sessionId: String,
  ): Flow<Result<PaginationData<MediaItem.Media>>> = combine(
    remote.fetchRatedTvShows(
      page = page,
      sortOption = sortOption,
      accountId = accountId,
      sessionId = sessionId,
    ),
    dao.getFavoriteMediaIds(MediaType.TV),
  ) { response, favoriteIds ->
    val data = response.map()
    val favoriteSet = favoriteIds.toSet()

    val updatedTvShows = data.list.map { media ->
      (media as MediaItem.Media.TV).copy(
        isFavorite = media.id in favoriteSet,
      )
    }

    Result.success(data.copy(list = updatedTvShows))
  }

  override suspend fun deleteEpisodeRating(
    showId: Int,
    season: Int,
    number: Int,
  ): Result<Unit> = remote.clearEpisodeRating(
    showId = showId,
    season = season,
    number = number,
  ).map { response ->
    if (response.success) {
      dao.deleteEpisodeRating(
        showId = showId,
        season = season,
        number = number,
      )
      Result.success(Unit)
    } else {
      Result.failure<Exception>(AppException.Unknown())
    }
  }

  override suspend fun submitEpisodeRating(
    showId: Int,
    season: Int,
    number: Int,
    rating: Int,
  ): Result<Unit> = remote.submitEpisodeRating(
    showId = showId,
    season = season,
    number = number,
    rating = rating,
  ).map { response ->
    if (response.success) {
      Result.success(Unit)
    } else {
      Result.failure(AppException.Unknown())
    }
  }
}
