package com.divinelink.core.navigation.route

import androidx.navigation.NavController

fun NavController.navigateToMediaLists(
  route: Navigation.MediaListsRoute,
) = navigate(
  route = route,
)
