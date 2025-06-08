package com.divinelink.core.navigation.route

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import kotlinx.serialization.Serializable

@Serializable
object SearchRoute

fun NavController.navigateToSearchFromTab(navOptions: NavOptions? = null) = navigate(
  navOptions = navOptions,
  route = SearchRoute,
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
    route = SearchRoute,
  )

  popBackStack(
    route = SearchRoute,
    inclusive = false,
  )
}
