package com.divinelink.core.network.session.util

import com.divinelink.core.network.Routes
import io.ktor.http.URLProtocol
import io.ktor.http.buildUrl
import io.ktor.http.encodedPath

fun buildCreateRequestTokenUrl(): String = buildUrl {
  protocol = URLProtocol.HTTPS
  host = Routes.TMDb.HOST
  encodedPath = Routes.TMDb.V4 + "/auth/request_token"
}.toString()

fun buildRequestTokenApproveUrl(token: String): String = buildUrl {
  protocol = URLProtocol.HTTPS
  host = "www.themoviedb.org"
  encodedPath = "auth/access"

  parameters.apply {
    append("request_token", token)
  }
}.toString()

fun buildAccessTokenUrl(): String = buildUrl {
  protocol = URLProtocol.HTTPS
  host = Routes.TMDb.HOST
  encodedPath = Routes.TMDb.V4 + "/auth/access_token"
}.toString()
