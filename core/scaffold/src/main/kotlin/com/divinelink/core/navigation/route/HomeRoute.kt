package com.divinelink.core.navigation.route

import androidx.navigation.NavController
import androidx.navigation.NavOptions

fun NavController.navigateToHome(navOptions: NavOptions) = navigate(
  route = Navigation.HomeRoute,
  navOptions = navOptions,
)
