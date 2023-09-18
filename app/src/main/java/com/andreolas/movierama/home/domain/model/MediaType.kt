package com.andreolas.movierama.home.domain.model

enum class MediaType(val value: String) {
  TV("tv"),
  MOVIE("movie"),
  PERSON("person"),
  UNKNOWN("unknown");

  companion object {
    fun from(type: String) = when (type) {
      TV.value -> TV
      MOVIE.value -> MOVIE
      PERSON.value -> PERSON
      else -> UNKNOWN
    }
  }
}
