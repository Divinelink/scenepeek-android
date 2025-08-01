package com.divinelink.core.navigation.route

import androidx.navigation.NavController

fun NavController.navigateToCreateList() = navigate(
  route = Navigation.CreateListRoute,
)

fun NavController.navigateToEditList(route: Navigation.EditListRoute) = navigate(
  route = route,
)
