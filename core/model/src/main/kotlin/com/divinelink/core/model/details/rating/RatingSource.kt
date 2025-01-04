package com.divinelink.core.model.details.rating

import com.divinelink.core.model.R

enum class RatingSource(
  val iconRes: Int,
  val value: String,
  val url: String,
) {
  TMDB(iconRes = R.drawable.core_model_ic_tmdb, value = "tmdb", url = "https://www.themoviedb.org"),
  IMDB(iconRes = R.drawable.core_model_ic_imdb, value = "imdb", url = "https://www.imdb.com"),
  TRAKT(iconRes = R.drawable.core_model_ic_trakt, value = "trakt", url = "https://trakt.tv"),
  ;

  companion object {
    fun from(value: String): RatingSource = entries.find { it.value == value } ?: TMDB
  }
}
