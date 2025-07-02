package com.divinelink.feature.lists.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.core.navigation.route.ListsRoute
import com.divinelink.feature.lists.ui.ListsScreen

fun NavGraphBuilder.listsScreen(onNavigateUp: () -> Unit) {
  composable<ListsRoute> {
    ListsScreen(
      onNavigateUp = onNavigateUp,
    )
  }
}
