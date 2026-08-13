package com.divinelink.core.network.opendata.util

import com.divinelink.core.network.Routes
import io.ktor.http.URLProtocol
import io.ktor.http.buildUrl
import io.ktor.http.encodedPath

fun buildWikidataEntitiesUrl(
  wikidataId: String,
): String = buildUrl {
  protocol = URLProtocol.HTTPS
  host = Routes.Wikidata.HOST
  encodedPath = Routes.Wikidata.API

  parameters.apply {
    append("action", "wbgetentities")
    append("props", "sitelinks")
    append("format", "json")
    append("ids", wikidataId)
  }
}.toString()
