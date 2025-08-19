package com.divinelink.core.data.account

import com.divinelink.core.database.media.dao.MediaDao
import com.divinelink.core.database.media.dao.checkIfMediaIsFavorite
import com.divinelink.core.model.PaginationData
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
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
    sortBy: String,
    accountId: String,
    sessionId: String,
  ): Flow<Result<PaginationData<MediaItem.Media>>> = combine(
    remote.fetchMoviesWatchlist(
      page = page,
      sortBy = sortBy,
      accountId = accountId,
      sessionId = sessionId,
    ),
    dao.fetchFavoriteMovieIds(),
  ) { response, _ ->
    val data = response.map()
    val updatedMovies = data.list.map { media ->
      (media as MediaItem.Media.Movie).copy(
        isFavorite = dao.checkIfMediaIsFavorite(media.id, MediaType.MOVIE),
      )
    }

    Result.success(data.copy(list = updatedMovies))
  }

  override suspend fun fetchTvShowsWatchlist(
    page: Int,
    sortBy: String,
    accountId: String,
    sessionId: String,
  ): Flow<Result<PaginationData<MediaItem.Media>>> = combine(
    remote.fetchTvShowsWatchlist(
      page = page,
      sortBy = sortBy,
      accountId = accountId,
      sessionId = sessionId,
    ),
    dao.fetchFavoriteTVIds(),
  ) { response, _ ->
    val data = response.map()
    val updatedTvShows = data.list.map { media ->
      (media as MediaItem.Media.TV).copy(
        isFavorite = dao.checkIfMediaIsFavorite(media.id, MediaType.TV),
      )
    }

    Result.success(data.copy(list = updatedTvShows))
  }

  override suspend fun fetchRatedMovies(
    page: Int,
    sortBy: String,
    accountId: String,
    sessionId: String,
  ): Flow<Result<PaginationData<MediaItem.Media>>> = combine(
    remote.fetchRatedMovies(
      page = page,
      sortBy = sortBy,
      accountId = accountId,
      sessionId = sessionId,
    ),
    dao.fetchFavoriteMovieIds(),
  ) { response, _ ->
    val data = response.map()
    val updatedMovies = data.list.map { media ->
      (media as MediaItem.Media.Movie).copy(
        isFavorite = dao.checkIfMediaIsFavorite(media.id, MediaType.MOVIE),
      )
    }

    Result.success(data.copy(list = updatedMovies))
  }

  override suspend fun fetchRatedTvShows(
    page: Int,
    sortBy: String,
    accountId: String,
    sessionId: String,
  ): Flow<Result<PaginationData<MediaItem.Media>>> = combine(
    remote.fetchRatedTvShows(
      page = page,
      sortBy = sortBy,
      accountId = accountId,
      sessionId = sessionId,
    ),
    dao.fetchFavoriteTVIds(),
  ) { response, _ ->
    val data = response.map()
    val updatedTvShows = data.list.map { media ->
      (media as MediaItem.Media.TV).copy(
        isFavorite = dao.checkIfMediaIsFavorite(media.id, MediaType.TV),
      )
    }

    Result.success(data.copy(list = updatedTvShows))
  }
}
