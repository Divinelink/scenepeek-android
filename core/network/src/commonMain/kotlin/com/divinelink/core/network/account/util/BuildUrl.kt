package com.divinelink.core.network.account.util

import com.divinelink.core.network.Routes
import io.ktor.http.URLProtocol
import io.ktor.http.buildUrl
import io.ktor.http.encodedPath

internal fun buildFetchListsUrl(
  accountId: String,
  page: Int,
): String = buildUrl {
  protocol = URLProtocol.HTTPS
  host = Routes.TMDb.HOST
  encodedPath = Routes.TMDb.V4 + "/account/$accountId/lists"

  parameters.apply {
    append("language", "en-US")
    append("page", page.toString())
  }
}.toString()

internal fun buildEpisodeRatingUrl(
  showId: Int,
  season: Int,
  number: Int,
  sessionId: String,
): String = buildUrl {
  protocol = URLProtocol.HTTPS
  host = Routes.TMDb.HOST
  encodedPath = Routes.TMDb.V3 + "/tv/$showId/season/$season/episode/$number/rating"

  parameters.apply {
    append("session_id", sessionId)
  }
}.toString()
