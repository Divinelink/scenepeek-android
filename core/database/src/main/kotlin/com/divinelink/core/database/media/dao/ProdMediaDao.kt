package com.divinelink.core.database.media.dao

import com.divinelink.core.database.Database
import com.divinelink.core.database.media.mapper.map
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaReference

class ProdMediaDao(private val database: Database) : SqlMediaDao {

  override fun insertMedia(media: MediaItem.Media) {
    database.mediaItemEntityQueries.insertMediaItem(media.map())
  }

  override fun insertMedia(media: List<MediaItem.Media>) {
    media.forEach {
      insertMedia(it)
    }
  }

  override fun fetchMedia(media: MediaReference): MediaItem.Media? = database
    .mediaItemEntityQueries
    .selectMediaItemByIdAndType(id = media.mediaId.toLong(), mediaType = media.mediaType.value)
    .executeAsOneOrNull()
    ?.map()
}
