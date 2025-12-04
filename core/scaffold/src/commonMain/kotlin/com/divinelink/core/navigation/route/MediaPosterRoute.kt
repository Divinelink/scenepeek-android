package com.divinelink.core.navigation.route

import androidx.navigation.NavController

fun NavController.navigateToPoster(route: Navigation.MediaPosterRoute) = navigate(
  route = route,
)
