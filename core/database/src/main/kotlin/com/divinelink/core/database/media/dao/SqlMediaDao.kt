package com.divinelink.core.database.media.dao

import com.divinelink.core.model.media.MediaItem

interface SqlMediaDao {

  fun fetchMediaItemById(mediaId: Int): MediaItem.Media?

  fun insertMedia(media: MediaItem.Media)

  fun insertMedia(media: List<MediaItem.Media>)
}
