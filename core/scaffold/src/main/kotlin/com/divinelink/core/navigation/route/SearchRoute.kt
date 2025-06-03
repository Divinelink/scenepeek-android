package com.divinelink.core.navigation.route

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable

@Serializable
data class SearchRoute(val focus: Boolean)

fun NavController.navigateToSearch(navOptions: NavOptions) = navigate(
  navOptions = navOptions,
  route = SearchRoute(false),
)

fun NavController.navigateToSearchAndFocus() = navigate(
  route = SearchRoute(true),
)
