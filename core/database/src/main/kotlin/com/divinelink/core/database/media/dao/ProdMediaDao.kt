package com.divinelink.core.database.media.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.database.Database
import com.divinelink.core.database.MediaItemEntity
import com.divinelink.core.database.currentEpochSeconds
import com.divinelink.core.database.media.mapper.map
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaReference
import com.divinelink.core.model.media.MediaType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Clock

class ProdMediaDao(
  private val database: Database,
  private val dispatcher: DispatcherProvider,
  private val clock: Clock,
) : MediaDao {

  override fun insertMedia(media: MediaItem.Media) = database.transaction {
    database.mediaItemEntityQueries.insertMediaItem(media.map())
  }

  override fun insertMediaList(media: List<MediaItem.Media>) {
    media.forEach {
      insertMedia(it)
    }
  }

  override fun insertMediaEntities(media: List<MediaItemEntity>) = database.transaction {
    media.forEach { database.mediaItemEntityQueries.insertMediaItem(it) }
  }

  override fun fetchMedia(media: MediaReference): MediaItem.Media? = database
    .mediaItemEntityQueries
    .selectMediaItemByIdAndType(id = media.mediaId.toLong(), mediaType = media.mediaType.value)
    .executeAsOneOrNull()
    ?.map()

  override fun fetchAllFavorites(): Flow<List<MediaItem.Media>> = database
    .favoriteMediaEntityQueries
    .getAllFavorites()
    .asFlow()
    .mapToList(dispatcher.io)
    .map { favorites ->
      favorites.mapNotNull { favorite ->
        if (MediaType.from(favorite.mediaType) == MediaType.MOVIE) {
          MediaItem.Media.Movie(
            id = favorite.id.toInt(),
            name = favorite.name,
            posterPath = favorite.posterPath,
            backdropPath = favorite.backdropPath,
            releaseDate = favorite.releaseDate!!,
            voteAverage = favorite.voteAverage,
            voteCount = favorite.voteCount.toInt(),
            overview = favorite.overview,
            popularity = favorite.popularity,
            isFavorite = true,
          )
        } else if (MediaType.from(favorite.mediaType) == MediaType.TV) {
          MediaItem.Media.TV(
            id = favorite.id.toInt(),
            name = favorite.name,
            posterPath = favorite.posterPath,
            backdropPath = favorite.backdropPath,
            releaseDate = favorite.firstAirDate!!,
            voteAverage = favorite.voteAverage,
            voteCount = favorite.voteCount.toInt(),
            overview = favorite.overview,
            popularity = favorite.popularity,
            isFavorite = true,
          )
        } else {
          null
        }
      }
    }

  override fun getFavoriteMediaIds(mediaType: MediaType): Flow<List<Int>> = database
    .favoriteMediaEntityQueries
    .getFavoriteMediaIds(mediaType.value)
    .asFlow()
    .mapToList(dispatcher.io)
    .map { result ->
      result.map { it.mediaId.toInt() }
    }

  override fun addToFavorites(
    mediaId: Int,
    mediaType: MediaType,
  ) = database.transaction {
    database.favoriteMediaEntityQueries.addToFavorites(
      mediaId = mediaId.toLong(),
      mediaType = mediaType.value,
      favoritedAt = clock.currentEpochSeconds(),
    )
  }

  override fun removeFromFavorites(
    mediaId: Int,
    mediaType: MediaType,
  ) = database.transaction {
    database.favoriteMediaEntityQueries.removeFromFavorites(
      mediaId.toLong(),
      mediaType.value,
    )
  }

  override fun isMediaFavorite(
    mediaId: Int,
    mediaType: MediaType,
  ) = database.transactionWithResult {
    database.favoriteMediaEntityQueries.isMediaFavorite(
      mediaId = mediaId.toLong(),
      mediaType = mediaType.value,
    ).executeAsOneOrNull() == true
  }
}
