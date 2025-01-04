@file:Suppress("ktlint:standard:trailing-comma-on-declaration-site")

package com.divinelink.core.model.media

/**
 * Represents the type of media.
 */
enum class MediaType(
  val value: String,
  val traktPath: String
) {
  TV(value = "tv", traktPath = "shows"),
  MOVIE(value = "movie", traktPath = "movies"),
  PERSON(value = "person", traktPath = "people"),
  UNKNOWN(value = "unknown", traktPath = "unknown");

  companion object {
    fun from(type: String) = when (type) {
      TV.value -> TV
      MOVIE.value -> MOVIE
      PERSON.value -> PERSON
      else -> UNKNOWN
    }

    fun isMedia(type: String) = from(type) != UNKNOWN && from(type) != PERSON
  }
}
