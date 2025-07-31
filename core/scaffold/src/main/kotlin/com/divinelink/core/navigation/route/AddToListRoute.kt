package com.divinelink.core.navigation.route

import androidx.navigation.NavController
import com.divinelink.core.model.media.MediaType
import kotlinx.serialization.Serializable

@Serializable
data class AddToListRoute(
  val id: Int,
  val mediaType: MediaType,
)

fun NavController.navigateToAddToList(route: AddToListRoute) = navigate(route = route)
