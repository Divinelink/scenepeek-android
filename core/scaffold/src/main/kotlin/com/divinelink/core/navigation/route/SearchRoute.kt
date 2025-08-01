package com.divinelink.core.navigation.route

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import com.divinelink.core.model.search.SearchEntryPoint

fun NavController.navigateToSearchFromTab(navOptions: NavOptions? = null) = navigate(
  navOptions = navOptions,
  route = Navigation.SearchRoute(
    entryPoint = SearchEntryPoint.SEARCH_TAB,
  ),
)

fun NavController.navigateToSearchFromHome() {
  navigate(
    navOptions = navOptions {
      popUpTo(this@navigateToSearchFromHome.graph.findStartDestination().id) {
        saveState = true
      }
      launchSingleTop = true
      restoreState = true
    },
    route = Navigation.SearchRoute(
      entryPoint = SearchEntryPoint.HOME,
    ),
  )

  popBackStack(
    route = Navigation.SearchRoute(entryPoint = SearchEntryPoint.HOME),
    inclusive = false,
  )
}
