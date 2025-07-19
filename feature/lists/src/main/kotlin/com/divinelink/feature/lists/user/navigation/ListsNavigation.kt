package com.divinelink.feature.lists.user.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.core.navigation.route.ListDetailsRoute
import com.divinelink.core.navigation.route.ListsRoute
import com.divinelink.feature.lists.user.ListsScreen

fun NavGraphBuilder.listsScreen(
  onNavigateUp: () -> Unit,
  onNavigateToTMDBLogin: () -> Unit,
  onNavigateToList: (ListDetailsRoute) -> Unit,
  onNavigateToCreateList: () -> Unit,
) {
  composable<ListsRoute> {
    ListsScreen(
      onNavigateUp = onNavigateUp,
      onNavigateToTMDBLogin = onNavigateToTMDBLogin,
      onNavigateToList = onNavigateToList,
      onNavigateToCreateList = onNavigateToCreateList,
    )
  }
}
