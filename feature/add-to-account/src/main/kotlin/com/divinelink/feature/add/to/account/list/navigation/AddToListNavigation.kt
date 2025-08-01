package com.divinelink.feature.add.to.account.list.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.navigation.route.Navigation.AddToListRoute
import com.divinelink.feature.add.to.account.list.ui.AddToListScreen

fun NavGraphBuilder.addToListScreen(onNavigate: (Navigation) -> Unit) {
  composable<AddToListRoute> {
    AddToListScreen(
      onNavigate = onNavigate,
    )
  }
}
