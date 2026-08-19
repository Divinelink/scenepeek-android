package com.divinelink.core.network.opendata.mapper

import com.divinelink.core.model.locale.LocaleCode
import com.divinelink.core.network.opendata.model.wikidata.WikidataEntitiesResponse

fun WikidataEntitiesResponse.map(wikidataId: String): Map<LocaleCode, String> = this
  .entities[wikidataId]
  ?.sitelinks
  ?.mapNotNull { (key, siteLink) ->
    val locale = key.take(2).lowercase()

    locale to siteLink.title.replace(" ", "_")
  }?.toMap() ?: emptyMap()
