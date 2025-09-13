package com.divinelink.feature.requests.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.feature.requests.ui.RequestsScreen

fun NavGraphBuilder.requestsScreen(onNavigate: (Navigation) -> Unit) {
  composable<Navigation.JellyseerrRequestsRoute> {
    RequestsScreen(
      onNavigate = onNavigate,
    )
  }
}
