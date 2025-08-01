package com.divinelink.core.navigation.route

import androidx.navigation.NavController
import com.divinelink.core.model.user.data.UserDataSection

fun NavController.navigateToUserData(section: UserDataSection) = navigate(
  route = Navigation.UserDataRoute(section),
)
