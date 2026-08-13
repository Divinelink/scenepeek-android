package com.divinelink.core.data.opendata

import com.divinelink.core.model.locale.LocaleCode
import com.divinelink.core.network.opendata.OpenDataService
import com.divinelink.core.network.opendata.mapper.map

class ProdOpenDataRepository(
  val openService: OpenDataService,
) : OpenDataRepository {

  override suspend fun getWikiTitles(wikidataId: String): Result<Map<LocaleCode, String>> =
    openService
      .getWikipediaTitle(wikidataId)
      .map { response -> response.map(wikidataId) }
}
