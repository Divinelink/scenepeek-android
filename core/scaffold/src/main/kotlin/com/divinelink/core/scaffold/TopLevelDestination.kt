package com.divinelink.core.scaffold

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.divinelink.core.model.search.SearchEntryPoint
import com.divinelink.core.navigation.route.Navigation

enum class TopLevelDestination(
  val selectedIcon: ImageVector,
  val unselectedIcon: ImageVector,
  val iconTextId: Int,
  val titleTextId: Int,
  val route: Navigation,
) {
  HOME(
    selectedIcon = Icons.Rounded.Home,
    unselectedIcon = Icons.Outlined.Home,
    iconTextId = R.string.home,
    titleTextId = R.string.home,
    route = Navigation.HomeRoute,
  ),
  SEARCH(
    selectedIcon = Icons.Filled.Search,
    unselectedIcon = Icons.Outlined.Search,
    iconTextId = R.string.search,
    titleTextId = R.string.search,
    route = Navigation.SearchRoute(
      entryPoint = SearchEntryPoint.SEARCH_TAB,
    ),
  ),
  PROFILE(
    selectedIcon = Icons.Rounded.Person,
    unselectedIcon = Icons.Outlined.Person,
    iconTextId = R.string.profile,
    titleTextId = R.string.profile,
    route = Navigation.ProfileRoute,
  ),
}
