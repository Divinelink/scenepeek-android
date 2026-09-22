package com.divinelink.core.database.media.mapper

import com.divinelink.core.database.saved.GetAllFavorites
import com.divinelink.core.database.saved.GetFavoritePeople
import com.divinelink.core.model.track.backup.MediaExport
import com.divinelink.core.model.track.backup.PersonExport

fun GetAllFavorites.map() = MediaExport(
  id = id,
  posterPath = posterPath,
  title = name,
  type = mediaType,
  savedAt = savedAt,
)

fun GetFavoritePeople.map() = PersonExport(
  id = id,
  profilePath = profilePath,
  name = name,
  gender = gender.toInt(),
  knownForDepartment = knownForDepartment,
  savedAt = savedAt,
)
