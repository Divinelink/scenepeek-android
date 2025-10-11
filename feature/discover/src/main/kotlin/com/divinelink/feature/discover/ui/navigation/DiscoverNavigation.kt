package com.divinelink.feature.discover.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.feature.discover.ui.DiscoverScreen

fun NavGraphBuilder.discoverScreen(onNavigate: (Navigation) -> Unit) {
  composable<Navigation.DiscoverRoute> {
    DiscoverScreen(
      onNavigate = onNavigate,
    )
  }
}
