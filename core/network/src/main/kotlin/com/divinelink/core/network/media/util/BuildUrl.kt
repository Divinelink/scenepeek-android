package com.divinelink.core.network.media.util

import com.divinelink.core.model.discover.DiscoverFilter
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

fun buildGenreUrl(media: MediaType): String = buildUrl {
  protocol = URLProtocol.HTTPS
  host = Routes.TMDb.HOST
  encodedPath = Routes.TMDb.V3 + "/genre/${media.value}/list"

  parameters.apply {
    append("language", "en")
  }
}.toString()

fun buildDiscoverUrl(
  page: Int,
  media: MediaType,
  filters: List<DiscoverFilter>,
): String = buildUrl {
  protocol = URLProtocol.HTTPS
  host = Routes.TMDb.HOST
  encodedPath = Routes.TMDb.V3 + "/discover/${media.value}"

  parameters.apply {
    append("page", page.toString())
    append("language", "en-US")
    append("include_adult", "false")
    append("vote_count.gte", "10")
    append("sort_by", "popularity.desc")
    filters.forEach { filter ->
      when (filter) {
        is DiscoverFilter.Genres -> if (filter.filters.isNotEmpty()) {
          append("with_genres", filter.filters.joinToString(","))
        }
        is DiscoverFilter.Language -> append("with_original_language", filter.language)
        is DiscoverFilter.Country -> append("with_origin_country", filter.countryCode)
        is DiscoverFilter.VoteAverage -> {
          append("vote_average.gte", filter.greaterThan.toString())
          append("vote_average.lte", filter.lessThan.toString())
        }
      }
    }
  }
}.toString()
