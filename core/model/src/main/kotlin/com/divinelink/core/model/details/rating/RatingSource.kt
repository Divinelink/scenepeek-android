package com.divinelink.core.model.details.rating

import com.divinelink.core.model.R

enum class RatingSource(
  val iconRes: Int,
  val value: String,
  val url: String,
) {
  TMDB(iconRes = R.drawable.core_model_ic_tmdb, value = "TMDB", url = "https://www.themoviedb.org"),
  IMDB(iconRes = R.drawable.core_model_ic_imdb, value = "IMDb", url = "https://www.imdb.com"),
  TRAKT(iconRes = R.drawable.core_model_ic_trakt, value = "Trakt", url = "https://trakt.tv"),
  ;

  companion object {
    fun from(value: String): RatingSource = entries.find { it.value == value } ?: TMDB
  }
}
