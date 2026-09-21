package com.divinelink.feature.episode

import com.divinelink.core.model.details.Episode
import com.divinelink.core.model.tab.EpisodeTab
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.snackbar.SnackbarMessage

data class EpisodeUiState(
  val showId: Long,
  val showTitle: String,
  val seasonTitle: String,
  val seasonNumber: Int,
  val episode: Episode?,
  val episodes: Map<Int, Episode>,
  val tabs: List<EpisodeTab>,
  val snackbarMessage: SnackbarMessage?,
  val ratingLoading: Boolean,
  val selectedIndex: Int,
) {
  companion object {
    fun initial(route: Navigation.Episode): EpisodeUiState {
      val (episodeIndex, seasonTitle, episode) = when (route) {
        is Navigation.Episode.FullScreenRoute -> Triple(
          route.episodeIndex,
          route.seasonTitle,
          null,
        )
        is Navigation.Episode.ModalRoute -> Triple(
          -1,
          "",
          route.episode,
        )
      }

      return EpisodeUiState(
        showId = route.showId,
        showTitle = route.showTitle,
        seasonNumber = route.seasonNumber,
        selectedIndex = episodeIndex,
        seasonTitle = seasonTitle,
        episodes = emptyMap(),
        tabs = emptyList(),
        episode = episode,
        snackbarMessage = null,
        ratingLoading = false,
      )
    }
  }
}
