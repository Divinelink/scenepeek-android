package com.divinelink.feature.episode

import com.divinelink.core.model.details.Episode
import com.divinelink.core.model.tab.EpisodeTab
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.snackbar.SnackbarMessage

data class EpisodeUiState(
  val showId: Int,
  val showTitle: String,
  val seasonTitle: String,
  val seasonNumber: Int,
  val selectedIndex: Int,
  val episode: Episode?,
  val episodes: Map<Int, Episode>,
  val tabs: List<EpisodeTab>,
  val snackbarMessage: SnackbarMessage?,
  val ratingLoading: Boolean,
) {
  companion object {
    fun initial(route: Navigation.EpisodeRoute) = EpisodeUiState(
      showId = route.showId,
      showTitle = route.showTitle,
      seasonTitle = route.seasonTitle,
      seasonNumber = route.seasonNumber,
      selectedIndex = route.episodeIndex,
      episodes = emptyMap(),
      tabs = emptyList(),
      episode = null,
      snackbarMessage = null,
      ratingLoading = false,
    )
  }
}
