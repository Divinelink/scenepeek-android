package com.divinelink.core.navigation.route

import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.person.Gender

fun Navigation.PersonRoute.map() = MediaItem.Person(
  id = id,
  knownForDepartment = knownForDepartment,
  name = name ?: "",
  profilePath = profilePath,
  gender = Gender.from(gender),
  role = listOf(PersonRole.Unknown),
  saved = saved,
)
