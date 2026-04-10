package com.divinelink.feature.season.ui.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.scaffold.TwoPaneScene
import com.divinelink.feature.season.ui.SeasonScreen

fun EntryProviderScope<Navigation>.seasonScreen(onNavigate: (Navigation) -> Unit) {
  entry<Navigation.SeasonRoute>(metadata = TwoPaneScene.twoPane()) { key ->
    LocalNavAnimatedContentScope.current.SeasonScreen(
      route = key,
      onNavigate = onNavigate,
    )
  }
}
