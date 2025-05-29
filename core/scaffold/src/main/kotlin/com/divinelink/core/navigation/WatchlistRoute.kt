package com.divinelink.core.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable

@Serializable
object WatchlistRoute

fun NavController.navigateToWatchlist(navOptions: NavOptions) = navigate(
  route = WatchlistRoute,
  navOptions = navOptions,
)
