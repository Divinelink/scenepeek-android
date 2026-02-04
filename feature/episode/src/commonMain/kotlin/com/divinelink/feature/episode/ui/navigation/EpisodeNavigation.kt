package com.divinelink.feature.episode.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.feature.episode.ui.EpisodeScreen

fun NavGraphBuilder.episodeScreen(onNavigate: (Navigation) -> Unit) {
  composable<Navigation.EpisodeRoute> {
    EpisodeScreen(
      onNavigate = onNavigate,
    )
  }
}
