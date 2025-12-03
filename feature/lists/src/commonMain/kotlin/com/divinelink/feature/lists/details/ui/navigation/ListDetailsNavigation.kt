package com.divinelink.feature.lists.details.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.navigation.route.Navigation.ListDetailsRoute
import com.divinelink.feature.lists.details.ui.ListDetailsScreen

fun NavGraphBuilder.listDetailsScreen(onNavigate: (Navigation) -> Unit) {
  composable<ListDetailsRoute> {
    ListDetailsScreen(
      onNavigate = onNavigate,
    )
  }
}
