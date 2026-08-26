package com.divinelink.core.database.opendata

import com.divinelink.core.model.media.MediaReference

interface OpenDataDao {

  fun saveMediaFromLetterboxdUrl(
    url: String,
    mediaId: Long?,
    mediaType: String?,
  )
  fun getMediaFromLetterboxdUrl(url: String): MediaReference?
}
