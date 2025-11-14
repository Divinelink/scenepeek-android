package com.divinelink.feature.lists.user.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.feature.lists.user.ListsScreen

fun NavGraphBuilder.listsScreen(onNavigate: (Navigation) -> Unit) {
  composable<Navigation.ListsRoute> {
    ListsScreen(
      onNavigate = onNavigate,
    )
  }
}
