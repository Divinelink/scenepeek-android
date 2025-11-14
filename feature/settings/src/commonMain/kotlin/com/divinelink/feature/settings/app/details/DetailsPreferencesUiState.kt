package com.divinelink.feature.settings.app.details

import com.divinelink.core.model.details.rating.RatingSource

data class DetailsPreferencesUiState(
  val movieSource: RatingSource,
  val tvSource: RatingSource,
  val episodesSource: RatingSource,
  val seasonsSource: RatingSource,
) {
  companion object {
    fun initial(): DetailsPreferencesUiState = DetailsPreferencesUiState(
      movieSource = RatingSource.TMDB,
      tvSource = RatingSource.TMDB,
      episodesSource = RatingSource.TMDB,
      seasonsSource = RatingSource.TMDB,
    )
  }
}
