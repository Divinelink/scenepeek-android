package com.divinelink.core.navigation.route

import androidx.navigation.NavController
import kotlinx.serialization.Serializable

@Serializable
data class ListDetailsRoute(
  val id: Int,
  val name: String,
)

fun NavController.navigateToListDetails(route: ListDetailsRoute) = navigate(
  route = route,
)
