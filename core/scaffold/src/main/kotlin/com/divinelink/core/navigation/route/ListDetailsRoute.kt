package com.divinelink.core.navigation.route

import androidx.navigation.NavController
import kotlinx.serialization.Serializable

@Serializable
object ListDetailsRoute

fun NavController.navigateToListDetails() = navigate(
  route = ListDetailsRoute,
)
