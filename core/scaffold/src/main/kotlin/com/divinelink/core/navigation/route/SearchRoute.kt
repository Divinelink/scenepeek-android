package com.divinelink.core.navigation.route

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable

@Serializable
data object SearchRoute

fun NavController.navigateToSearch(navOptions: NavOptions) = navigate(
  navOptions = navOptions,
  route = SearchRoute,
)
