package com.divinelink.feature.add.to.account.list.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.core.navigation.route.AddToListRoute
import com.divinelink.feature.add.to.account.list.ui.AddToListScreen

fun NavGraphBuilder.addToListScreen(
  onNavigateUp: () -> Unit,
  onNavigateToTMDBAuth: () -> Unit,
  onNavigateToCreateList: () -> Unit,
) {
  composable<AddToListRoute> {
    AddToListScreen(
      onNavigateUp = onNavigateUp,
      onNavigateToTMDBAuth = onNavigateToTMDBAuth,
      onNavigateToCreateList = onNavigateToCreateList,
    )
  }
}
