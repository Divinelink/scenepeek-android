package com.divinelink.core.scaffold

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.Bookmarks
import androidx.compose.material.icons.rounded.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.divinelink.core.navigation.route.HomeRoute
import com.divinelink.core.navigation.route.SearchRoute
import com.divinelink.core.navigation.route.WatchlistRoute

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
  WATCHLIST(
    selectedIcon = Icons.Rounded.Bookmarks,
    unselectedIcon = Icons.Outlined.Bookmarks,
    iconTextId = R.string.watchlist,
    titleTextId = R.string.watchlist,
    route = WatchlistRoute,
  ),
}
