package com.divinelink.core.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable

@Serializable
object HomeRoute

fun NavController.navigateToHome(navOptions: NavOptions) = navigate(
  route = HomeRoute,
  navOptions = navOptions,
)
