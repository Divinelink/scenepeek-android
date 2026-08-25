package com.divinelink.core.network.opendata

import com.divinelink.core.network.client.RestClient
import com.divinelink.core.network.client.get
import com.divinelink.core.network.opendata.model.wikidata.WikidataEntitiesResponse
import com.divinelink.core.network.opendata.util.buildWikidataEntitiesUrl
import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.network.parseGetRequest

class ProdOpenDataService(
  val client: RestClient,
) : OpenDataService {

  override suspend fun getWikipediaTitle(wikidataId: String): Result<WikidataEntitiesResponse> =
    runCatching {
      client.client.get<WikidataEntitiesResponse>(
        url = buildWikidataEntitiesUrl(wikidataId),
      )
    }

  override suspend fun getIdFromLetterboxd(url: String): Result<Pair<Long, String>> = runCatching {
    val doc = Ksoup.parseGetRequest(url).body()

    val id = doc.attr("data-tmdb-id").takeIf { it.isNotEmpty() } ?: error("id not found")
    val mediaType = doc.attr("data-tmdb-type").takeIf { it.isNotEmpty() } ?: error("type not found")

    id.toLong() to mediaType
  }
}
