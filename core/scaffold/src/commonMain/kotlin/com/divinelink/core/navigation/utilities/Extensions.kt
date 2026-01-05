package com.divinelink.core.navigation.utilities

import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.navigation.route.Navigation

fun MediaItem.toRoute(): Navigation? = when (this) {
  is MediaItem.Media -> Navigation.DetailsRoute(
    id = id,
    mediaType = mediaType.value,
    isFavorite = isFavorite,
  )
  is MediaItem.Person -> Navigation.PersonRoute(
    id = id.toLong(),
    knownForDepartment = knownForDepartment,
    name = name,
    profilePath = profilePath,
    gender = gender.value,
  )
  else -> null
}
