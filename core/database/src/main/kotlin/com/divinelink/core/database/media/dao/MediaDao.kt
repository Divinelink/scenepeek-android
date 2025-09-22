package com.divinelink.core.database.media.dao

import com.divinelink.core.database.MediaItemEntity
import com.divinelink.core.model.details.Season
import com.divinelink.core.model.jellyseerr.media.SeasonRequest
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaReference
import com.divinelink.core.model.media.MediaType
import kotlinx.coroutines.flow.Flow

interface MediaDao {

  fun fetchMedia(media: MediaReference): MediaItem.Media?

  fun insertMedia(
    media: MediaItem.Media,
    seasons: List<Season>?,
  )

  fun insertMediaEntities(media: List<MediaItemEntity>)

  fun insertMediaList(media: List<MediaItem.Media>)

  fun fetchAllFavorites(): Flow<List<MediaItem.Media>>

  fun getFavoriteMediaIds(mediaType: MediaType): Flow<List<Int>>

  fun insertSeasons(
    id: Int,
    seasons: List<Season>,
  )

  fun updateSeasonStatus(
    mediaId: Int,
    seasons: List<SeasonRequest>,
    override: Boolean,
  )

  fun fetchSeasons(id: Int): Flow<List<Season>>

  fun addToFavorites(
    mediaId: Int,
    mediaType: MediaType,
  )

  fun removeFromFavorites(
    mediaId: Int,
    mediaType: MediaType,
  )

  fun isMediaFavorite(
    mediaId: Int,
    mediaType: MediaType,
  ): Boolean
}
