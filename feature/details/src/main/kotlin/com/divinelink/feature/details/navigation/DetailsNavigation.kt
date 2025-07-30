package com.divinelink.feature.details.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.feature.details.media.ui.DetailsScreen

fun NavGraphBuilder.detailsScreen(onNavigate: (Navigation) -> Unit) {
  composable<Navigation.DetailsRoute> {
    DetailsScreen(
      onNavigate = onNavigate,
      animatedVisibilityScope = this@composable,
    )
  }
}
