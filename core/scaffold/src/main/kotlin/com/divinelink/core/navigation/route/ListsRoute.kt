package com.divinelink.core.navigation.route

import androidx.navigation.NavController
import kotlinx.serialization.Serializable

@Serializable
object ListsRoute

fun NavController.navigateToLists() = navigate(
  route = ListsRoute,
)
