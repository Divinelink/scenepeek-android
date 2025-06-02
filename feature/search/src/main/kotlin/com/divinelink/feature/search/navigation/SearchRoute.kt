package com.divinelink.feature.search.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.core.navigation.route.SearchRoute
import com.divinelink.feature.search.ui.SearchScreen

fun NavGraphBuilder.searchScreen(onNavigateUp: () -> Unit) {
  composable<SearchRoute> {
    SearchScreen(
      onNavigateUp = onNavigateUp,
    )
  }
}
