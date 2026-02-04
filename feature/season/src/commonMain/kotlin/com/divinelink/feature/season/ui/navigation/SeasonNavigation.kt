package com.divinelink.feature.season.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.feature.season.ui.SeasonScreen

fun NavGraphBuilder.seasonScreen(onNavigate: (Navigation) -> Unit) {
  composable<Navigation.SeasonRoute> {
    SeasonScreen(
      onNavigate = onNavigate,
    )
  }
}
