package com.divinelink.core.network.trakt.util

import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.Routes
import io.ktor.http.URLProtocol
import io.ktor.http.appendEncodedPathSegments
import io.ktor.http.buildUrl

fun buildTraktRatingUrl(
  mediaType: MediaType,
  imdbId: String,
): String = buildUrl {
  protocol = URLProtocol.HTTPS
  host = Routes.Trakt.HOST

  val path = when (mediaType) {
    MediaType.MOVIE -> "movies"
    MediaType.TV -> "shows"
    else -> throw IllegalArgumentException("Unsupported media type")
  }

  appendEncodedPathSegments(path)
  appendEncodedPathSegments(imdbId)
  appendEncodedPathSegments("ratings")
}.toString()
