package com.andreolas.movierama.home.domain.model

enum class MediaType(val type: String) {
  TV("tv"),
  MOVIE("movie"),
  PERSON("person"),
  UNKNOWN("unknown");

  companion object {
    fun from(type: String) = when (type) {
      TV.type -> TV
      MOVIE.type -> MOVIE
      PERSON.type -> PERSON
      else -> UNKNOWN
    }
  }
}
