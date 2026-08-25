package com.divinelink.core.data.opendata

import com.divinelink.core.model.locale.LocaleCode
import com.divinelink.core.model.media.MediaReference
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.opendata.OpenDataService
import com.divinelink.core.network.opendata.mapper.map

class ProdOpenDataRepository(
  val openService: OpenDataService,
) : OpenDataRepository {

  override suspend fun getWikiTitles(wikidataId: String): Result<Map<LocaleCode, String>> =
    openService
      .getWikipediaTitle(wikidataId)
      .map { response -> response.map(wikidataId) }

  override suspend fun getMediaFromLetterboxd(url: String): Result<MediaReference> = openService
    .getIdFromLetterboxd(url).map { pair ->
      MediaReference(
        mediaId = pair.first,
        mediaType = MediaType.from(pair.second),
      )
    }
}
