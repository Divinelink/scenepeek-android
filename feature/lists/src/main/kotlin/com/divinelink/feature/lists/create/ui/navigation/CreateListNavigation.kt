package com.divinelink.feature.lists.create.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.core.navigation.route.CreateListRoute
import com.divinelink.feature.lists.create.ui.CreateListScreen

fun NavGraphBuilder.createListScreen(onNavigateUp: () -> Unit) {
  composable<CreateListRoute> {
    CreateListScreen(
      onNavigateUp = onNavigateUp,
    )
  }
}
