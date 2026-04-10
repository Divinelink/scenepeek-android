package com.divinelink.feature.episode.ui.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.scaffold.TwoPaneScene
import com.divinelink.feature.episode.ui.EpisodeScreen

fun EntryProviderScope<Navigation>.episodeScreen(onNavigate: (Navigation) -> Unit) {
  entry<Navigation.EpisodeRoute>(metadata = TwoPaneScene.twoPane()) { key ->
    LocalNavAnimatedContentScope.current.EpisodeScreen(
      route = key,
      onNavigate = onNavigate,
    )
  }
}
