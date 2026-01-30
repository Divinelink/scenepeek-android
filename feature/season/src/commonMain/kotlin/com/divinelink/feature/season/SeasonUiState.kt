package com.divinelink.feature.season

import com.divinelink.core.model.details.Season
import com.divinelink.core.model.details.season.SeasonForm
import com.divinelink.core.model.details.season.SeasonForms
import com.divinelink.core.model.tab.SeasonTab
import com.divinelink.core.model.tab.Tab
import com.divinelink.core.navigation.route.Navigation

data class SeasonUiState(
  val backdropPath: String?,
  val title: String,
  val season: Season?,
  val tabs: List<Tab>,
  val selectedTab: Int,
  val forms: SeasonForms,
) {
  companion object {
    fun initial(route: Navigation.SeasonRoute) = SeasonUiState(
      backdropPath = route.backdropPath,
      title = route.title,
      season = null,
      selectedTab = 0,
      tabs = SeasonTab.entries,
      forms = SeasonTab.entries.associateWith { tab ->
        when (tab) {
          SeasonTab.Episodes -> SeasonForm.Loading
          SeasonTab.About -> SeasonForm.Loading
          SeasonTab.Cast -> SeasonForm.Loading
          SeasonTab.GuestStars -> SeasonForm.Loading
        }
      },
    )
  }
}
