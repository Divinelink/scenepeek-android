package com.divinelink.core.network.list.util

import com.divinelink.core.network.Routes
import io.ktor.http.URLProtocol
import io.ktor.http.buildUrl
import io.ktor.http.encodedPath

internal fun buildAddItemsToListUrl(listId: Int): String = buildUrl {
  protocol = URLProtocol.HTTPS
  host = Routes.TMDb.HOST
  encodedPath = Routes.TMDb.V4 + "/list/$listId/items"
}.toString()

internal fun buildFetchListDetailsUrl(
  listId: Int,
  page: Int,
): String = buildUrl {
  protocol = URLProtocol.HTTPS
  host = Routes.TMDb.HOST
  encodedPath = Routes.TMDb.V4 + "/list/$listId"

  parameters.apply {
    append("language", "en-US")
    append("page", page.toString())
  }
}.toString()

internal fun buildListUrl(): String = buildUrl {
  protocol = URLProtocol.HTTPS
  host = Routes.TMDb.HOST
  encodedPath = Routes.TMDb.V4 + "/list"
}.toString()

internal fun buildListWithIdUrl(id: Int): String = buildUrl {
  protocol = URLProtocol.HTTPS
  host = Routes.TMDb.HOST
  encodedPath = Routes.TMDb.V4 + "/list/$id"
}.toString()
