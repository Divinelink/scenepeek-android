package com.divinelink.core.navigation.route

import androidx.navigation.NavController
import com.divinelink.core.model.media.MediaType
import kotlinx.serialization.Serializable

fun NavController.navigateToDetails(route: DetailsRoute) = navigate(route = route)

@Serializable
data class DetailsRoute(
  val id: Int,
  val mediaType: MediaType,
  val isFavorite: Boolean?,
)
