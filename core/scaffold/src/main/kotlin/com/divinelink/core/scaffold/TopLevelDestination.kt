package com.divinelink.core.scaffold

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.rounded.Bookmarks
import androidx.compose.material.icons.rounded.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.divinelink.core.navigation.HomeRoute
import com.divinelink.core.navigation.WatchlistRoute

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
  WATCHLIST(
    selectedIcon = Icons.Rounded.Bookmarks,
    unselectedIcon = Icons.Outlined.Bookmarks,
    // TODO Add resources from the feature module
    iconTextId = R.string.watchlist,
    titleTextId = R.string.watchlist,
    route = WatchlistRoute,
  ),
}
