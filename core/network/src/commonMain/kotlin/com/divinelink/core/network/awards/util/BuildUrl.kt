package com.divinelink.core.network.awards.util

import com.divinelink.core.network.Routes
import io.ktor.http.URLProtocol
import io.ktor.http.buildUrl
import io.ktor.http.encodedPath

fun buildAwardCeremoniesUrl(): String = buildUrl {
  protocol = URLProtocol.HTTPS
  host = Routes.Awards.HOST
  encodedPath = "ceremonies.json"
}.toString()

fun buildCeremonyCategoriesUrl(id: String): String = buildUrl {
  protocol = URLProtocol.HTTPS
  host = Routes.Awards.HOST
  encodedPath = "$id/categories.json"
}.toString()

fun buildGetAwardsCategoryUrl(
  ceremonyId: String,
  categoryId: String,
): String = buildUrl {
  protocol = URLProtocol.HTTPS
  host = Routes.Awards.HOST
  encodedPath = "$ceremonyId/category/$categoryId.json"
}.toString()
