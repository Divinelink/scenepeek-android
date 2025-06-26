package com.divinelink.core.navigation.route

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable

@Serializable
object ProfileRoute

fun NavController.navigateToProfile(navOptions: NavOptions) = navigate(
  route = ProfileRoute,
  navOptions = navOptions,
)
