package com.divinelink.core.network.opendata

import com.divinelink.core.network.opendata.model.wikidata.WikidataEntitiesResponse

interface OpenDataService {

  suspend fun getWikipediaTitle(wikidataId: String): Result<WikidataEntitiesResponse>

  suspend fun getIdFromLetterboxd(url: String): Result<Pair<Long, String>>
}
