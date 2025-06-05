package com.divinelink.feature.search.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.core.navigation.route.DetailsRoute
import com.divinelink.core.navigation.route.PersonRoute
import com.divinelink.core.navigation.route.SearchRoute
import com.divinelink.feature.search.ui.SearchScreen

fun NavGraphBuilder.searchScreen(
  onNavigateToSettings: () -> Unit,
  onNavigateToDetails: (DetailsRoute) -> Unit,
  onNavigateToPerson: (PersonRoute) -> Unit,
) {
  composable<SearchRoute> {
    SearchScreen(
      onNavigateToSettings = onNavigateToSettings,
      onNavigateToDetails = onNavigateToDetails,
      onNavigateToPerson = onNavigateToPerson,
    )
  }
}
