package com.andreolas.movierama.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.rounded.Bookmarks
import androidx.compose.material.icons.rounded.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.andreolas.movierama.R
import com.divinelink.ui.screens.destinations.HomeScreenDestination
import com.ramcosta.composedestinations.generated.watchlist.destinations.WatchlistScreenDestination
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec

enum class TopLevelDestination(
  val selectedIcon: ImageVector,
  val unselectedIcon: ImageVector,
  val iconTextId: Int,
  val titleTextId: Int,
  val direction: DirectionDestinationSpec
) {
  HOME(
    selectedIcon = Icons.Rounded.Home,
    unselectedIcon = Icons.Outlined.Home,
    iconTextId = R.string.home,
    titleTextId = R.string.home,
    direction = HomeScreenDestination
  ),
  WATCHLIST(
    selectedIcon = Icons.Rounded.Bookmarks,
    unselectedIcon = Icons.Outlined.Bookmarks,
    iconTextId = R.string.watchlist, // TODO Add resources from the feature module
    titleTextId = R.string.watchlist,
    direction = WatchlistScreenDestination
  )
}
