package com.divinelink.core.network.opendata

import com.divinelink.core.network.client.RestClient
import com.divinelink.core.network.client.get
import com.divinelink.core.network.opendata.model.wikidata.WikidataEntitiesResponse
import com.divinelink.core.network.opendata.util.buildWikidataEntitiesUrl

class ProdOpenDataService(
  val client: RestClient,
) : OpenDataService {

  override suspend fun getWikipediaTitle(wikidataId: String): Result<WikidataEntitiesResponse> =
    runCatching {
      client.client.get<WikidataEntitiesResponse>(
        url = buildWikidataEntitiesUrl(wikidataId),
      )
    }
}
