package com.divinelink.core.navigation.route

import androidx.navigation.NavController
import com.divinelink.core.model.media.MediaReference
import com.divinelink.core.model.media.MediaType
import kotlinx.serialization.Serializable

@Serializable
data class AddToListRoute(
  val id: Int,
  val mediaType: MediaType,
) {
  constructor(
    media: MediaReference,
  ) : this(
    id = media.mediaId,
    mediaType = media.mediaType,
  )
}

fun NavController.navigateToAddToList(route: AddToListRoute) = navigate(route = route)
