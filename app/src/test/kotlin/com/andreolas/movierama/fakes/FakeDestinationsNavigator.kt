package com.andreolas.movierama.fakes

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import com.google.common.truth.Truth.assertThat
import com.ramcosta.composedestinations.navigation.DestinationsNavOptionsBuilder
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.Direction
import com.ramcosta.composedestinations.spec.RouteOrDirection

class FakeDestinationsNavigator : DestinationsNavigator {
  private val navigatedRoutes = mutableListOf<String>()

  override fun clearBackStack(route: RouteOrDirection): Boolean {
    navigatedRoutes.clear()
    return true
  }

  override fun navigate(direction: Direction, builder: DestinationsNavOptionsBuilder.() -> Unit) {
    navigatedRoutes.add(direction.route)
  }

  override fun navigate(
    direction: Direction,
    navOptions: NavOptions?,
    navigatorExtras: Navigator.Extras?
  ) {
    navigatedRoutes.add(direction.route)
  }

  override fun navigateUp(): Boolean {
    navigatedRoutes.removeLast()
    return true
  }

  override fun popBackStack(): Boolean {
    navigatedRoutes.removeLast()
    return true
  }

  override fun popBackStack(
    route: RouteOrDirection,
    inclusive: Boolean,
    saveState: Boolean
  ): Boolean {
    TODO("Not yet implemented")
  }

  override fun getBackStackEntry(route: RouteOrDirection): NavBackStackEntry? {
    TODO()
  }

  fun verifyNavigatedToDirection(expectedDirection: Direction) {
    val expectedRoute = expectedDirection.route

    assertThat(navigatedRoutes).contains(expectedRoute)
  }
}
