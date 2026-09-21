package com.divinelink.core.database.media.mapper

import com.divinelink.core.database.favorite.GetAllFavorites
import com.divinelink.core.database.favorite.GetFavoritePeople
import com.divinelink.core.model.track.backup.MediaExport
import com.divinelink.core.model.track.backup.PersonExport

fun GetAllFavorites.map() = MediaExport(
  id = id,
  posterPath = posterPath,
  title = name,
  type = mediaType,
  savedAt = favoritedAt ?: "",
)

fun GetFavoritePeople.map() = PersonExport(
  id = id,
  profilePath = profilePath,
  name = name,
  gender = gender.toInt(),
  knownForDepartment = knownForDepartment,
  savedAt = favoritedAt ?: "",
)
