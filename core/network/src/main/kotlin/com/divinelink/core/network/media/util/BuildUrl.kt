package com.divinelink.core.network.media.util

import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.Routes
import io.ktor.http.URLProtocol
import io.ktor.http.buildUrl
import io.ktor.http.encodedPath

fun buildFetchDetailsUrl(
  id: Int,
  media: MediaType,
): String = buildUrl {
  protocol = URLProtocol.HTTPS
  host = Routes.TMDb.HOST
  encodedPath = Routes.TMDb.V3 + "/${media.value}" + "/$id"

  parameters.apply {
    append("language", "en-US")
    when (media) {
      MediaType.MOVIE -> append("append_to_response", "credits")
      MediaType.TV -> append("append_to_response", "external_ids")
      else -> {
        // Do nothing
      }
    }
  }
}.toString()
