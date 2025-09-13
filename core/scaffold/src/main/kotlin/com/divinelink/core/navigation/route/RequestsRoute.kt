package com.divinelink.core.navigation.route

import androidx.navigation.NavController

fun NavController.navigateToRequests() = navigate(
  route = Navigation.JellyseerrRequestsRoute,
)
