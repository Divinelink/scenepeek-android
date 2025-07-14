package com.divinelink.feature.lists.details.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.core.navigation.route.ListDetailsRoute
import com.divinelink.feature.lists.details.ui.ListDetailsScreen

fun NavGraphBuilder.listDetailsScreen(onNavigateUp: () -> Unit) {
  composable<ListDetailsRoute> {
    ListDetailsScreen(
      onNavigateUp = onNavigateUp,
    )
  }
}
