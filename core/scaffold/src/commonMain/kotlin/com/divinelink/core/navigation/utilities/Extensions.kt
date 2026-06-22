package com.divinelink.core.navigation.utilities

import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaReference
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.person.Gender
import com.divinelink.core.navigation.route.Navigation

fun MediaItem.toRoute(): Navigation? = when (this) {
  is MediaItem.Media -> Navigation.DetailsRoute(
    id = id,
    mediaType = mediaType.value,
    isFavorite = isFavorite,
  )
  is MediaItem.Person -> Navigation.PersonRoute(
    id = id,
    knownForDepartment = knownForDepartment,
    name = name,
    profilePath = profilePath,
    gender = gender.value,
  )
  else -> null
}

fun MediaReference.toRoute(): Navigation? = when (this.mediaType) {
  MediaType.TV,
  MediaType.MOVIE,
    -> Navigation.DetailsRoute(
    id = mediaId,
    mediaType = mediaType.value,
    isFavorite = false,
  )
  MediaType.PERSON -> Navigation.PersonRoute(
    id = mediaId,
    knownForDepartment = null,
    name = null,
    profilePath = null,
    gender = Gender.NOT_SET.value,
  )
  MediaType.UNKNOWN -> null
}

fun Navigation.isSameTopLevelDestination(other: Navigation): Boolean =
  this::class == other::class && other.isTopLevelDestination()

private fun Navigation.isTopLevelDestination() = this is Navigation.HomeRoute ||
  this is Navigation.ProfileRoute ||
  this is Navigation.SearchRoute
