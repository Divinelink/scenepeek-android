package com.divinelink.core.model.details.rating

import com.divinelink.core.model.Res
import com.divinelink.core.model.core_model_ic_imdb
import com.divinelink.core.model.core_model_ic_tmdb
import com.divinelink.core.model.core_model_ic_trakt
import org.jetbrains.compose.resources.DrawableResource

enum class RatingSource(
  val iconRes: DrawableResource,
  val value: String,
  val url: String,
) {
  TMDB(
    iconRes = Res.drawable.core_model_ic_tmdb,
    value = "TMDB",
    url = "https://www.themoviedb.org",
  ),
  IMDB(iconRes = Res.drawable.core_model_ic_imdb, value = "IMDb", url = "https://www.imdb.com"),
  TRAKT(iconRes = Res.drawable.core_model_ic_trakt, value = "Trakt", url = "https://trakt.tv"),
  ;

  companion object {
    fun from(value: String): RatingSource = entries.find { it.value == value } ?: TMDB
  }
}
