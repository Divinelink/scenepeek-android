package com.divinelink.core.model.details.rating

import com.divinelink.core.model.R

enum class RatingSource(
  val iconRes: Int,
  val value: String,
) {
  TMDB(iconRes = R.drawable.core_model_ic_tmdb, value = "tmdb"),
  IMDB(iconRes = R.drawable.core_model_ic_imdb, value = "imdb"),
  TRAKT(iconRes = R.drawable.core_model_ic_trakt, value = "trakt"),
  ;

  companion object {
    fun from(value: String): RatingSource = entries.find { it.value == value } ?: TMDB
  }
}
