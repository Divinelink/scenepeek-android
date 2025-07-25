package com.divinelink.core.database.media.dao

import com.divinelink.core.database.Database
import com.divinelink.core.database.media.mapper.map
import com.divinelink.core.model.media.MediaItem

class ProdMediaDao(private val database: Database) : SqlMediaDao {

  override fun insertMedia(media: List<MediaItem.Media>) {
    media.forEach {
      database.mediaItemEntityQueries.insertMediaItem(it.map())
    }
  }
}
