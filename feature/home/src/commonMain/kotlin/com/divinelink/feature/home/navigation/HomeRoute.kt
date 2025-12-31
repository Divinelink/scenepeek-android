package com.divinelink.feature.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.feature.home.HomeScreen

fun NavGraphBuilder.homeScreen(onNavigate: (Navigation) -> Unit) {
  composable<Navigation.HomeRoute> {
    HomeScreen(
      onNavigate = onNavigate,
      animatedVisibilityScope = this@composable,
    )
  }
}
