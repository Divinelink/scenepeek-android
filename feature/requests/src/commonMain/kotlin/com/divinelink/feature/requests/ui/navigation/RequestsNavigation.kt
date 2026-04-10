package com.divinelink.feature.requests.ui.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.scaffold.TwoPaneScene
import com.divinelink.feature.requests.ui.RequestsScreen

fun EntryProviderScope<Navigation>.requestsScreen(onNavigate: (Navigation) -> Unit) {
  entry<Navigation.JellyseerrRequestsRoute>(metadata = TwoPaneScene.twoPane()) { _ ->
    LocalNavAnimatedContentScope.current.RequestsScreen(
      onNavigate = onNavigate,
    )
  }
}
