package com.divinelink.core.database.credits.mapper

import com.divinelink.core.database.cast.FetchShowCast
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.person.Gender

fun FetchShowCast.toPerson(roles: List<PersonRole>): MediaItem.Person = MediaItem.Person(
  id = id,
  name = name,
  profilePath = profilePath,
  knownForDepartment = knownForDepartment,
  gender = Gender.from(gender.toInt()),
  role = roles,
)
