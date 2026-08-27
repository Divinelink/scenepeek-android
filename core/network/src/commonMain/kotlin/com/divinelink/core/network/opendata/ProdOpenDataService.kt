package com.divinelink.core.network.opendata

import com.divinelink.core.commons.extensions.extractAttributes
import com.divinelink.core.model.exception.NonTMDBContentException
import com.divinelink.core.network.client.RestClient
import com.divinelink.core.network.client.get
import com.divinelink.core.network.opendata.model.wikidata.WikidataEntitiesResponse
import com.divinelink.core.network.opendata.util.buildWikidataEntitiesUrl
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

class ProdOpenDataService(
  val restClient: RestClient,
  val client: HttpClient,
) : OpenDataService {

  override suspend fun getWikipediaTitle(wikidataId: String): Result<WikidataEntitiesResponse> =
    runCatching {
      restClient.client.get<WikidataEntitiesResponse>(
        url = buildWikidataEntitiesUrl(wikidataId),
      )
    }

  override suspend fun getIdFromLetterboxd(url: String): Result<Pair<Long, String>> = runCatching {
    val doc = client.get(urlString = url).bodyAsText()
    val idKey = "data-tmdb-id"
    val typeKey = "data-tmdb-type"

    val attrs = doc.extractAttributes(idKey, typeKey)

    val id = attrs[idKey] ?: throw NonTMDBContentException()
    val mediaType = attrs[typeKey] ?: throw NonTMDBContentException()

    id.toLong() to mediaType
  }
}
