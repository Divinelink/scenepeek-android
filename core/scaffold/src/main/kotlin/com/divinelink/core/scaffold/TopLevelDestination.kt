package com.divinelink.core.scaffold

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.divinelink.core.navigation.route.HomeRoute
import com.divinelink.core.navigation.route.ProfileRoute
import com.divinelink.core.navigation.route.SearchRoute

enum class TopLevelDestination(
  val selectedIcon: ImageVector,
  val unselectedIcon: ImageVector,
  val iconTextId: Int,
  val titleTextId: Int,
  val route: Any,
) {
  HOME(
    selectedIcon = Icons.Rounded.Home,
    unselectedIcon = Icons.Outlined.Home,
    iconTextId = R.string.home,
    titleTextId = R.string.home,
    route = HomeRoute,
  ),
  SEARCH(
    selectedIcon = Icons.Filled.Search,
    unselectedIcon = Icons.Outlined.Search,
    iconTextId = R.string.search,
    titleTextId = R.string.search,
    route = SearchRoute,
  ),
  PROFILE(
    selectedIcon = Icons.Rounded.Person,
    unselectedIcon = Icons.Outlined.Person,
    iconTextId = R.string.profile,
    titleTextId = R.string.profile,
    route = ProfileRoute,
  ),
}
