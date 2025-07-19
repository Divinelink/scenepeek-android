package com.divinelink.core.navigation.route

import androidx.navigation.NavController
import kotlinx.serialization.Serializable

@Serializable
object CreateListRoute

fun NavController.navigateToCreateList() = navigate(
  route = CreateListRoute,
)
