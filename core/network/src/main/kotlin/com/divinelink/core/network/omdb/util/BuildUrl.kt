package com.divinelink.core.network.omdb.util

import com.divinelink.core.network.BuildConfig
import com.divinelink.core.network.Routes
import io.ktor.http.URLProtocol
import io.ktor.http.buildUrl

fun buildOMDbUrl(
  imdbId: String,
  apikey: String = BuildConfig.OMDB_API_KEY,
): String = buildUrl {
  protocol = URLProtocol.HTTPS
  host = Routes.OMDb.HOST
  parameters.apply {
    append("i", imdbId)
    append("apikey", apikey)
  }
}.toString()
