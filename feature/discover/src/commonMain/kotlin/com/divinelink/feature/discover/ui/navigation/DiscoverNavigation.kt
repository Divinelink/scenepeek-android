package com.divinelink.feature.discover.ui.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.scaffold.TwoPaneScene
import com.divinelink.feature.discover.ui.DiscoverScreen

fun EntryProviderScope<Navigation>.discoverScreen(onNavigate: (Navigation) -> Unit) {
  entry<Navigation.DiscoverRoute>(metadata = TwoPaneScene.twoPane()) { key ->
    LocalNavAnimatedContentScope.current.DiscoverScreen(
      route = key,
      onNavigate = onNavigate,
    )
  }
}
