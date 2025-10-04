package com.divinelink.core.network.media.util

import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.Routes
import io.ktor.http.URLProtocol
import io.ktor.http.buildUrl
import io.ktor.http.encodedPath

fun buildFetchDetailsUrl(
  id: Int,
  media: MediaType,
  appendToResponse: Boolean,
): String = buildUrl {
  protocol = URLProtocol.HTTPS
  host = Routes.TMDb.HOST
  encodedPath = Routes.TMDb.V3 + "/${media.value}" + "/$id"

  parameters.apply {
    append("language", "en-US")
    if (appendToResponse) {
      when (media) {
        MediaType.MOVIE -> append("append_to_response", "credits")
        MediaType.TV -> append("append_to_response", "external_ids")
        else -> {
          // Do nothing
        }
      }
    }
  }
}.toString()

fun buildFindByIdUrl(
  externalId: String,
  externalSource: String = "imdb_id",
): String = buildUrl {
  protocol = URLProtocol.HTTPS
  host = Routes.TMDb.HOST
  encodedPath = Routes.TMDb.V3 + "/find" + "/$externalId"

  parameters.apply {
    append("external_source", externalSource)
  }
}.toString()

fun buildMovieGenreUrl(): String = buildUrl {
  protocol = URLProtocol.HTTPS
  host = Routes.TMDb.HOST
  encodedPath = Routes.TMDb.V3 + "/genre/movie/list"

  parameters.apply {
    append("language", "en")
  }
}.toString()

fun buildTvGenreUrl(): String = buildUrl {
  protocol = URLProtocol.HTTPS
  host = Routes.TMDb.HOST
  encodedPath = Routes.TMDb.V3 + "/genre/tv/list"

  parameters.apply {
    append("language", "en")
  }
}.toString()
