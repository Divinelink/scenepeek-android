package com.divinelink.core.navigation.route

import androidx.navigation.NavController

fun NavController.navigateToLists() = navigate(
  route = Navigation.ListsRoute,
)
