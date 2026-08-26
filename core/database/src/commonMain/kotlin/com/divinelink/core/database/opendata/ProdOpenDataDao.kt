package com.divinelink.core.database.opendata

import com.divinelink.core.database.Database
import com.divinelink.core.model.media.MediaReference
import com.divinelink.core.model.media.MediaType

class ProdOpenDataDao(private val database: Database) : OpenDataDao {

  override fun getMediaFromLetterboxdUrl(url: String): MediaReference? = with(database) {
    letterboxdIdEntityQueries
      .getMediaFromUrl(url = url)
      .executeAsOneOrNull()
      ?.let { media ->
        MediaReference(
          mediaType = MediaType.from(media.mediaType),
          mediaId = media.mediaId ?: -1,
        )
      }
  }

  override fun saveMediaFromLetterboxdUrl(
    url: String,
    mediaId: Long?,
    mediaType: String?,
  ) = with(database) {
    transaction {
      letterboxdIdEntityQueries.insertMedia(
        url = url,
        mediaId = mediaId,
        mediaType = mediaType,
      )
    }
  }
}
