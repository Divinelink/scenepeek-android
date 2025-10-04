package com.divinelink.feature.discover

import com.divinelink.core.model.tab.MediaTab

data class DiscoverUiState(
  val selectedTabIndex: Int,
  val tabs: List<MediaTab>,
) {
  companion object {
    val initial = DiscoverUiState(
      selectedTabIndex = MediaTab.Movie.order,
      tabs = MediaTab.entries,
    )
  }
}
