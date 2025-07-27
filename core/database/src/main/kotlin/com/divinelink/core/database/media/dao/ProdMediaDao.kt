package com.divinelink.core.database.media.dao

import com.divinelink.core.database.Database
import com.divinelink.core.database.media.mapper.map
import com.divinelink.core.model.media.MediaItem

class ProdMediaDao(private val database: Database) : SqlMediaDao {

  override fun insertMedia(media: MediaItem.Media) {
    database.mediaItemEntityQueries.insertMediaItem(media.map())
  }

  override fun insertMedia(media: List<MediaItem.Media>) {
    media.forEach {
      insertMedia(it)
    }
  }

  override fun fetchMediaItemById(mediaId: Int): MediaItem.Media? = database
    .mediaItemEntityQueries
    .selectMediaItemById(mediaId.toLong())
    .executeAsOneOrNull()
    ?.map()
}
