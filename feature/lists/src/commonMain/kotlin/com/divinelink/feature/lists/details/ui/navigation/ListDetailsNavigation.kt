package com.divinelink.feature.lists.details.ui.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.navigation.route.Navigation.ListDetailsRoute
import com.divinelink.core.scaffold.TwoPaneScene
import com.divinelink.feature.lists.details.ui.ListDetailsScreen

fun EntryProviderScope<Navigation>.listDetailsScreen(onNavigate: (Navigation) -> Unit) {
  entry<ListDetailsRoute>(metadata = TwoPaneScene.twoPane()) { key ->
    LocalNavAnimatedContentScope.current.ListDetailsScreen(
      route = key,
      onNavigate = onNavigate,
    )
  }
}
