package com.divinelink.core.navigation.route

import androidx.navigation.NavController

fun NavController.navigateToEpisode(route: Navigation.EpisodeRoute) = navigate(
  route = route,
)
