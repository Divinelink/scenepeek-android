package com.divinelink.feature.settings.app.details

import com.divinelink.core.model.details.rating.RatingSource
import com.divinelink.core.model.preferences.DetailPreferences

data class DetailsPreferencesUiState(
  val detailPreferences: DetailPreferences,
  val movieSource: RatingSource,
  val tvSource: RatingSource,
  val episodesSource: RatingSource,
  val seasonsSource: RatingSource,
) {
  companion object {
    fun initial(): DetailsPreferencesUiState = DetailsPreferencesUiState(
      detailPreferences = DetailPreferences.initial,
      movieSource = RatingSource.TMDB,
      tvSource = RatingSource.TMDB,
      episodesSource = RatingSource.TMDB,
      seasonsSource = RatingSource.TMDB,
    )
  }
}
