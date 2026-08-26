package com.divinelink.core.data.opendata

import com.divinelink.core.database.opendata.OpenDataDao
import com.divinelink.core.model.exception.NonTMDBContentException
import com.divinelink.core.model.locale.LocaleCode
import com.divinelink.core.model.media.MediaReference
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.opendata.OpenDataService
import com.divinelink.core.network.opendata.mapper.map

class ProdOpenDataRepository(
  val openService: OpenDataService,
  val openDataDao: OpenDataDao,
) : OpenDataRepository {

  override suspend fun getWikiTitles(wikidataId: String): Result<Map<LocaleCode, String>> =
    openService
      .getWikipediaTitle(wikidataId)
      .map { response -> response.map(wikidataId) }

  override suspend fun getMediaFromLetterboxd(url: String): Result<MediaReference> = openDataDao
    .getMediaFromLetterboxdUrl(url)
    ?.let { Result.success(it) }
    ?: run {
      openService
        .getIdFromLetterboxd(url)
        .fold(
          onSuccess = { data ->
            openDataDao.saveMediaFromLetterboxdUrl(
              url = url,
              mediaId = data.first,
              mediaType = data.second,
            )

            val media = MediaReference(
              mediaId = data.first,
              mediaType = MediaType.from(data.second),
            )

            Result.success(media)
          },
          onFailure = { error ->
            if (error is NonTMDBContentException) {
              openDataDao.saveMediaFromLetterboxdUrl(url = url, mediaId = null, mediaType = null)
            }
            Result.failure(error)
          },
        )
    }
}
