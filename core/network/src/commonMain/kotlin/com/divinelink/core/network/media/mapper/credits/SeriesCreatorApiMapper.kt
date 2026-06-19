package com.divinelink.core.network.media.mapper.credits

import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.person.Gender
import com.divinelink.core.network.media.model.details.credits.SeriesCreatorApi

fun List<SeriesCreatorApi>.map(): List<MediaItem.Person> = map { it.map() }

fun SeriesCreatorApi.map() = MediaItem.Person(
  id = id.toInt(),
  name = name,
  profilePath = profilePath,
  gender = Gender.from(gender),
  knownForDepartment = knownForDepartment,
  role = listOf(PersonRole.Creator),
)
