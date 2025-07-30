package com.divinelink.feature.search.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.feature.search.ui.SearchScreen

fun NavGraphBuilder.searchScreen(onNavigate: (Navigation) -> Unit) {
  composable<Navigation.SearchRoute> {
    SearchScreen(onNavigate = onNavigate)
  }
}
