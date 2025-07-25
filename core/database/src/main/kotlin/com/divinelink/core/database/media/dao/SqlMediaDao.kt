package com.divinelink.core.database.media.dao

import com.divinelink.core.model.media.MediaItem

interface SqlMediaDao {

  fun insertMedia(media: List<MediaItem.Media>)
}
