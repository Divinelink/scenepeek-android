package com.divinelink.core.network.account.util

import com.divinelink.core.network.Routes
import io.ktor.http.URLProtocol
import io.ktor.http.buildUrl
import io.ktor.http.encodedPath

internal fun buildFetchListsUrl(accountId: String): String = buildUrl {
  protocol = URLProtocol.HTTPS
  host = Routes.TMDb.HOST
  encodedPath = Routes.TMDb.V4 + "/account/$accountId/lists"
}.toString()
