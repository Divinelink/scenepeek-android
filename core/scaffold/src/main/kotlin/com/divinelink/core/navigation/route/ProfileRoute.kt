package com.divinelink.core.navigation.route

import androidx.navigation.NavController
import androidx.navigation.NavOptions

fun NavController.navigateToProfile(navOptions: NavOptions) = navigate(
  route = Navigation.ProfileRoute,
  navOptions = navOptions,
)
