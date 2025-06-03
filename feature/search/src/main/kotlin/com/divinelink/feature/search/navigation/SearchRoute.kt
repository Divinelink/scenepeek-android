package com.divinelink.feature.search.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.divinelink.core.navigation.route.SearchRoute
import com.divinelink.feature.search.ui.SearchScreen

fun NavGraphBuilder.searchScreen(onNavigateToSettings: () -> Unit) {
  composable<SearchRoute> {
    val searchRoute = it.toRoute<SearchRoute>()

    SearchScreen(
      onNavigateToSettings = onNavigateToSettings,
      focus = searchRoute.focus,
    )
  }
}
