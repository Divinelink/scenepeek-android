package com.divinelink.core.database.person.mapper

import com.divinelink.core.database.person.PersonDetailsEntity
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.person.Gender

fun PersonDetailsEntity.map() = MediaItem.Person(
  id = id,
  name = name,
  profilePath = profilePath,
  gender = Gender.from(gender.toInt()),
  knownForDepartment = knownForDepartment,
  role = listOf(PersonRole.Unknown),
  saved = false,
)
