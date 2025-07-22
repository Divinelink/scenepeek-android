package com.divinelink.core.navigation.route

import androidx.navigation.NavController
import kotlinx.serialization.Serializable

@Serializable
object CreateListRoute

fun NavController.navigateToCreateList() = navigate(
  route = CreateListRoute,
)

@Serializable
data class EditListRoute(
  val id: Int,
  val name: String,
  val backdropPath: String?,
  val description: String,
  val public: Boolean,
)

fun NavController.navigateToEditList(route: EditListRoute) = navigate(
  route = route,
)
