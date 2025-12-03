package com.divinelink.core.navigation.route

import androidx.navigation.NavController

fun NavController.openDefaultActionMenuModal(route: Navigation.ActionMenuRoute.Media) =
  navigate(route)
