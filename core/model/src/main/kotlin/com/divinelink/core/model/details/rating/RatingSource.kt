package com.divinelink.core.model.details.rating

enum class RatingSource(val value: String) {
  TMDB("tmdb"),
  IMDB("imdb"),
  TRAKT("trakt");

  companion object {
    fun from(value: String): RatingSource = entries.find { it.value == value } ?: TMDB
  }
}
