package com.divinelink.core.navigation.route

import androidx.navigation.NavController
import kotlinx.serialization.Serializable

@Serializable
object WatchlistRoute

fun NavController.navigateToWatchlist() = navigate(route = WatchlistRoute)
