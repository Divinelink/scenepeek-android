package com.divinelink.core.model.person.credits

import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.media.MediaItem

data class PersonCredit(
  val creditId: String,
  val media: MediaItem.Media,
  val role: PersonRole,
)
