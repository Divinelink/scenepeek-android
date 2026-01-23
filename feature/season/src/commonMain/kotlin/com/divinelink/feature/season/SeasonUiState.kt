package com.divinelink.feature.season

import com.divinelink.core.model.details.Season
import com.divinelink.core.navigation.route.Navigation

data class SeasonUiState(
  val backdropPath: String?,
  val title: String,
  val season: Season?,
) {
  companion object {
    fun initial(route: Navigation.SeasonRoute) = SeasonUiState(
      backdropPath = route.backdropPath,
      title = route.title,
      season = null,
    )
  }
}
