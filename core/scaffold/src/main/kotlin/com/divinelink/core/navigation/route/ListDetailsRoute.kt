package com.divinelink.core.navigation.route

import androidx.navigation.NavController
import kotlinx.serialization.Serializable

@Serializable
data class ListDetailsRoute(
  val id: Int,
  val name: String,
  val backdropPath: String?,
  val description: String,
  val public: Boolean,
)

fun NavController.navigateToListDetails(route: ListDetailsRoute) = navigate(
  route = route,
)
