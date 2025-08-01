package com.divinelink.feature.lists.create.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.feature.lists.create.ui.CreateListScreen

fun NavGraphBuilder.createListScreen(onNavigateUp: () -> Unit) {
  composable<Navigation.CreateListRoute> {
    CreateListScreen(
      onNavigateUp = onNavigateUp,
      onNavigateBackToLists = {},
    )
  }
}

fun NavGraphBuilder.editListScreen(
  onNavigateUp: () -> Unit,
  onNavigateBackToLists: () -> Unit,
) {
  composable<Navigation.EditListRoute> {
    CreateListScreen(
      onNavigateUp = onNavigateUp,
      onNavigateBackToLists = onNavigateBackToLists,
    )
  }
}
