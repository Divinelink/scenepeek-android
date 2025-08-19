package com.divinelink.core.database.media.dao

import com.divinelink.core.database.MediaItemEntity
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaReference

interface SqlMediaDao {

  fun fetchMedia(media: MediaReference): MediaItem.Media?

  fun insertMedia(media: MediaItem.Media)

  fun insertMediaEntities(media: List<MediaItemEntity>)

  fun insertMedia(media: List<MediaItem.Media>)
}
