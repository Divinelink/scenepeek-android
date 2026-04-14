package com.divinelink.core.navigation

import androidx.compose.runtime.mutableStateListOf
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.navigation.utilities.isSameDestinationType

class Navigator {
  val backStack = mutableStateListOf<Navigation>(Navigation.HomeRoute)

  fun navigate(destination: Navigation) {
    if (backStack.lastOrNull()?.isSameDestinationType(destination) == true) return

    when (destination) {
      Navigation.Back -> goBack()
      else -> {
        when (destination) {
          is Navigation.HomeRoute,
          is Navigation.SearchRoute,
          is Navigation.ProfileRoute,
            -> popToTopLevelDestination(destination)
          else -> backStack.add(destination)
        }
      }
    }
  }

  private fun popToTopLevelDestination(destination: Navigation) {
    val existing = backStack.firstOrNull { it.isSameDestinationType(destination) }
    val filtered = backStack.filter { !it.isSameDestinationType(destination) }
    backStack.clear()
    backStack.addAll(filtered)
    backStack.add(existing ?: destination)
  }

  fun clear() {
    retainTopLevelDestinations()
  }

  private fun retainTopLevelDestinations() {
    val topLevelDestinations = backStack.filter {
      it is Navigation.HomeRoute ||
        it is Navigation.SearchRoute ||
        it is Navigation.ProfileRoute
    }

    val finalDestinations = topLevelDestinations.ifEmpty { listOf(Navigation.HomeRoute) }

    backStack.clear()
    backStack.addAll(finalDestinations)
  }

  fun goBack() {
    if (backStack.size <= 1) return
    backStack.removeLastOrNull()
  }
}
